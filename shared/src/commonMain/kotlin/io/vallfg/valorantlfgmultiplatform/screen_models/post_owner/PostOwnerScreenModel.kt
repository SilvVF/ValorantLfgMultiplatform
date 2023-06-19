package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import kotlinx.coroutines.launch

class PostOwnerScreenModel(
    private val websocketsRepo: WebsocketsRepo
): StateScreenModel<PostOwnerScreenState>(PostOwnerScreenState.Loading) {

    fun connect(
        minRank: Rank,
        gameMode: GameMode,
        needed: Int
    ) {
        coroutineScope.launch {
            websocketsRepo.createPost(minRank, gameMode, needed) {
                mutableState.value = PostOwnerScreenState.Failure("disconnected")
            }
                .onSuccess {
                    mutableState.value = PostOwnerScreenState.Success()
                    websocketsRepo.receiveFlow.collect { data ->
                        when (data) {
                            is Message ->  mutableState.value = PostOwnerScreenState.Success(
                                messages = state.value.messages + data
                            )

                            else -> {}
                        }
                    }
                }
                .onFailure {
                    mutableState.value = PostOwnerScreenState.Failure(it.message ?: "Error")
                }
        }
    }
}