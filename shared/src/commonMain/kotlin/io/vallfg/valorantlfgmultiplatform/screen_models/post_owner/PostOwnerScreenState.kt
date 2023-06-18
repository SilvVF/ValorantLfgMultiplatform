package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

sealed class PostOwnerScreenState (

) {

    object Loading: PostOwnerScreenState()

    object Success: PostOwnerScreenState()

    data class Failure(
        val message: String
    ): PostOwnerScreenState()
}