package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import io.vallfg.valorantlfgmultiplatform.network.Message

sealed class PostOwnerScreenState (
    open val messages: List<Message> = emptyList()
) {

    object Loading: PostOwnerScreenState()

    data class Success(
        override val messages: List<Message> = emptyList()
    ): PostOwnerScreenState(messages)

    data class Failure(
        val message: String
    ): PostOwnerScreenState()
}