package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

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
import io.vallfg.valorantlfgmultiplatform.network.PostCreate
import io.vallfg.valorantlfgmultiplatform.network.PostState
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class PostOwnerScreenModel(
    private val websocketsRepo: WebsocketsRepo
): StateScreenModel<PostOwnerScreenState>(PostOwnerScreenState.Loading) {

    private val eventChannel = Channel<PostOwnerScreenEvent>()
    val events = eventChannel.receiveAsFlow()

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

    private fun onReceived(data: OutWsData) {
        when (data) {
            is Message -> {
                mutableState.value.updateSuccess { state ->
                    state.copy(
                        messages = state.messages + data
                    )
                }
            }
            is Error -> {}
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
            is PostClosed -> TODO()
            is PostCreate -> TODO()
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
                        messages = data.messages,
                        users = data.users.associateBy { it.clientId }
                    )
                }
            }
        }
    }
}