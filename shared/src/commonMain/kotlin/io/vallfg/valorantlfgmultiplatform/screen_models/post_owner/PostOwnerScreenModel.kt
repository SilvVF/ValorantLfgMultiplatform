package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.domain.usecase.InsertFailedMessageUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.SendMessageUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.suspendOnError
import io.vallfg.valorantlfgmultiplatform.domain.usecase.suspendOnSuccess
import io.vallfg.valorantlfgmultiplatform.network.Error
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.OutWsData
import io.vallfg.valorantlfgmultiplatform.network.PlayerJoined
import io.vallfg.valorantlfgmultiplatform.network.PlayerLeft
import io.vallfg.valorantlfgmultiplatform.network.PostClosed
import io.vallfg.valorantlfgmultiplatform.network.PostJoined
import io.vallfg.valorantlfgmultiplatform.network.PostState
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PostOwnerScreenModel(
    private val websocketsRepo: WebsocketsRepo,
    private val sendMessageUseCase: SendMessageUseCase,
    private val insertFailedMessageUseCase: InsertFailedMessageUseCase
): StateScreenModel<PostOwnerScreenState>(PostOwnerScreenState.Loading) {

    private val eventChannel = Channel<PostOwnerScreenEvent>()
    val events = eventChannel.receiveAsFlow()

    private val messageLoadingJobs = mutableMapOf<Int, Job?>()

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

    private fun sendMessage(text: String) = coroutineScope.launch {
        sendMessageUseCase(
            websocketsRepo = websocketsRepo,
            text = text,
            users = state.value.users,
            clientId = state.value.clientId
        )
            .suspendOnSuccess {
                messageLoadingJobs[it.id] = updateMessageOnTimeoutJob(it.id)
                mutableState.updateSuccess { state ->
                    state.copy(
                        messages = state.messages + it
                    )
                }
            }
            .suspendOnError { errors ->
                eventChannel.send(
                    PostOwnerScreenEvent.Error(
                        errors.firstOrNull()?.message ?: "Unknown Error"
                    )
                )
            }
    }

    private fun updateMessageOnTimeoutJob(
        id: Int,
    ) = coroutineScope.launch {
        delay(12000)
        mutableState.updateSuccess { state ->
            state.copy(
                messages = insertFailedMessageUseCase(
                    id = id,
                    messages = state.messages
                )
            )
        }
    }

    private suspend fun onReceived(data: OutWsData) {
        when (data) {
            is Message -> {
                mutableState.updateSuccess { state ->
                    state.copy(
                        messages = state.messages + toUiMessage(data)
                    ).also {
                        if (data.sender.clientId == state.clientId) {
                            messageLoadingJobs[data.sendId]?.cancel()
                        }
                    }
                }
            }
            is Error -> {
                eventChannel.send(
                    PostOwnerScreenEvent.Error(data.message)
                )
            }
            is PlayerJoined -> {
                mutableState.updateSuccess { state ->
                    state.copy(
                        users = buildMap<String, WsPlayerData> {
                            putAll(state.users)
                            put(data.player.clientId, data.player)
                        }
                    )
                }
            }
            is PlayerLeft -> {
                mutableState.updateSuccess { state ->
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
                mutableState.updateSuccess {
                    it.copy(
                        postId = data.id,
                        clientId = data.clientId
                    )
                }
            }
            is PostState -> {
                mutableState.updateSuccess { state ->
                    state.copy(
                        postState = PostState(
                            creator = data.creator,
                            banned = data.banned,
                            minRank = Rank.fromString(data.minRank),
                            needed = data.needed,
                            gameMode = GameMode.fromString(data.gameMode)
                        ),
                        messages = data.messages.map(::toUiMessage),
                        users = buildMap {
                            putAll(data.users.associateBy { it.clientId })
                            putAll(data.users.associateBy { "hello" })
                        }
                    )
                }
            }
        }
    }

    private fun resendFailedMessage(
        message: UiMessage.Failed
    ) = coroutineScope.launch {
            removeFailedMessage(message)
            sendMessage(message.text)
    }

    private fun removeFailedMessage(
        message: UiMessage.Failed
    ) = coroutineScope.launch {
        mutableState.updateSuccess { state ->
            state.copy(
                messages = state.messages.filter { it != message }
            )
        }
    }

    private suspend fun MutableStateFlow<PostOwnerScreenState>.updateSuccess(
        action: (state: PostOwnerScreenState.Success) -> PostOwnerScreenState
    ) {
        (this.value as? PostOwnerScreenState.Success)?.let {
            val result = action(it)
            this.emit(result)
        }
    }

    private fun toUiMessage(message: Message): UiMessage {
        return if (message.sender.clientId == state.value.clientId) {
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