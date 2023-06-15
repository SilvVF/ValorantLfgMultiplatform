package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.suspendOnFailure
import io.vallfg.valorantlfgmultiplatform.network.suspendOnSuccess
import kotlinx.coroutines.launch

class PostViewScreenModel(
    private val apiRepo: ApiRepo,
    private val navigator: DestinationsNavigator
): StateScreenModel<PostViewState>(PostViewState.Loading) {

    private var currentPostOffset = 0

    init {
        coroutineScope.launch {
            apiRepo.getPost(40, 0)
                .suspendOnSuccess {
                    mutableState.value = PostViewState.Success(posts = it.posts)
                    currentPostOffset = 40
                }
                .suspendOnFailure {
                    mutableState.value = PostViewState.Error(message = it.joinToString())
                }
        }
    }

    fun refresh() = coroutineScope.launch {
        mutableState.value = PostViewState.Loading
        apiRepo.getPost(25, currentPostOffset)
            .suspendOnSuccess {q ->
                val state = (mutableState.value as? PostViewState.Success) ?: return@suspendOnSuccess
                mutableState.value = state.copy(posts = state.posts + q.posts)
                currentPostOffset += 25
            }
            .suspendOnFailure {
                mutableState.value = PostViewState.Error(it.joinToString())
            }
    }
}