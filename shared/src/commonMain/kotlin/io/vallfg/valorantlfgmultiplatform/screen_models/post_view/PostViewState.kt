package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import io.vallfg.PostQuery

sealed class PostViewState {

    object Loading: PostViewState()

    data class Success(
        val posts: List<PostQuery.Post>
    ): PostViewState()

    data class Error(val message: String): PostViewState()
}
