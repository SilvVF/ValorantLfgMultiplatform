package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import cafe.adriel.voyager.core.concurrent.AtomicInt32
import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.network.Error
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.OutWsData
import io.vallfg.valorantlfgmultiplatform.network.PlayerJoined
import io.vallfg.valorantlfgmultiplatform.network.PlayerLeft
import io.vallfg.valorantlfgmultiplatform.network.PostClosed
import io.vallfg.valorantlfgmultiplatform.network.PostJoined
import io.vallfg.valorantlfgmultiplatform.network.PostState
import io.vallfg.valorantlfgmultiplatform.network.SendMessage
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PostOwnerScreenModel(
    private val websocketsRepo: WebsocketsRepo,
): StateScreenModel<PostOwnerScreenState>(PostOwnerScreenState.Loading) {

    private val eventChannel = Channel<PostOwnerScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private val messageLoadingJobs = mutableMapOf<Int, Job?>()

    private fun PostOwnerScreenState.updateSuccess(action: (state: PostOwnerScreenState.Success) -> PostOwnerScreenState) {
        val state = this as? PostOwnerScreenState.Success
        state?.let {
            mutableState.value = action(it)
        }
    }

    fun connect(
        minRank: Rank,
        gameMode: GameMode,
        needed: Int
    ) {
        coroutineScope.launch {
            websocketsRepo.createPost(
                minRank, gameMode, needed,
                onDisconnect = {
                    mutableState.value = PostOwnerScreenState.Failure("disconnected")
                },
                onError = { error ->
                    eventChannel.send(
                        PostOwnerScreenEvent.Error(error.message ?: "Unknown error")
                    )
                },
                onReceived =  {
                    onReceived(it)
                }
            )
                .onSuccess {
                    mutableState.value = PostOwnerScreenState.Success()
                }
                .onFailure {
                    mutableState.value = PostOwnerScreenState.Failure(it.message ?: "Error")
                }
        }
    }

    private fun sendMessage(text: String) {
        coroutineScope.launch {
            val id = messageId.getAndIncrement()
            websocketsRepo.send(
                SendMessage(text, id)
            )
                .onSuccess {
                    mutableState.value.updateSuccess { state ->
                        val message = UiMessage.Outgoing(
                            id = id,
                            sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                            loading = true,
                            message = Message(
                                text = text,
                                sender = state.users[state.clientId] ?: WsPlayerData.emptyPlayer,
                                sentAtEpochSecond = 0L,
                                sendId = id
                            )
                        )
                        messageLoadingJobs[id] = updateMessageOnTimeoutJob(id, text)
                        state.copy(messages = state.messages + message)
                    }
                }
                .onFailure {
                    eventChannel.send(
                        PostOwnerScreenEvent.Error(it.message ?: "Unknown Error")
                    )
                }
        }
    }

    private fun removeTimeoutJob(id: Int) {
        messageLoadingJobs[id]?.cancel()
        messageLoadingJobs.remove(id)
    }

    private fun updateMessageOnTimeoutJob(id: Int, text: String, timoutMillis: Long = 10000) = coroutineScope.launch {
        delay(timoutMillis)
        mutableState.value.updateSuccess {
            val idxOfSend = it.messages.indexOfFirst { msg ->
                when (msg) {
                    is UiMessage.Failed -> false
                    is UiMessage.Incoming -> false
                    is UiMessage.Outgoing -> msg.id == id
                }
            }
            it.copy(
                messages = buildList {
                    addAll(it.messages.subList(0, idxOfSend))
                    add(UiMessage.Failed(text))
                    addAll(it.messages.subList(idxOfSend + 1, it.messages.size))
                }
            )
        }
    }

    private suspend fun onReceived(data: OutWsData) {
        when (data) {
            is Message -> {
                removeTimeoutJob(data.sendId)
                mutableState.value.updateSuccess { state ->
                    state.copy(
                        messages = state.messages + toUiMessage(data)
                    )
                }
            }
            is Error -> {
                eventChannel.send(
                    PostOwnerScreenEvent.Error(data.message)
                )
            }
            is PlayerJoined -> {
                mutableState.value.updateSuccess { state ->
                    state.copy(
                        users = buildMap<String, WsPlayerData> {
                            putAll(state.users)
                            put(data.player.clientId, data.player)
                        }
                    )
                }
            }
            is PlayerLeft -> {
                mutableState.value.updateSuccess { state ->
                    state.copy(
                        users = state.users.filter { (id, player) ->
                            id != data.player.clientId
                        }
                    )
                }
            }
            is PostClosed -> {
                mutableState.value = PostOwnerScreenState.Closed(
                    messages = data.messages,
                    users = data.users.associateBy { it.clientId },
                    postState = PostState(
                        creator = data.creator,
                        banned = data.banned,
                        minRank = Rank.fromString(data.minRank),
                        needed = data.needed,
                        gameMode = GameMode.fromString(data.gameMode)
                    )
                )
            }
            is PostJoined -> {
                mutableState.value.updateSuccess {
                    it.copy(
                        postId = data.id,
                        clientId = data.clientId
                    )
                }
            }
            is PostState -> {
                mutableState.value.updateSuccess { state ->
                    state.copy(
                        postState = PostState(
                            creator = data.creator,
                            banned = data.banned,
                            minRank = Rank.fromString(data.minRank),
                            needed = data.needed,
                            gameMode = GameMode.fromString(data.gameMode)
                        ),
                        messages = data.messages.map(::toUiMessage),
                        users = data.users.associateBy { it.clientId }
                    )
                }
            }
        }
    }

    private val messageId = AtomicInt32(0)

    private fun resendFailedMessage(message: UiMessage.Failed) {
        coroutineScope.launch {
            removeFailedMessage(message)
            sendMessage(message.text)
        }
    }

    private fun removeFailedMessage(message: UiMessage.Failed) {
        coroutineScope.launch {
            mutableState.value.updateSuccess { state ->
                state.copy(
                    messages = state.messages.filter { it != message }
                )
            }
        }
    }

    private fun toUiMessage(message: Message): UiMessage {
        return if (message.sender.clientId == (mutableState.value as? PostOwnerScreenState.Success)?.clientId) {
            UiMessage.Outgoing(
                id = -1,
                message = message,
                loading = false,
                sentAt = Instant.fromEpochSeconds(message.sentAtEpochSecond).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ),
            )
        } else {
            UiMessage.Incoming(message)
        }
    }
}