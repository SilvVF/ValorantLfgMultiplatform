package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.domain.usecase.ApplyPostFiltersUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.FilterPostsUseCase
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.suspendOnFailure
import io.vallfg.valorantlfgmultiplatform.network.suspendOnSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PostViewScreenModel(
    private val apiRepo: ApiRepo,
    private val navigator: DestinationsNavigator,
    private val applyPostFiltersUseCase: ApplyPostFiltersUseCase,
    private val filterPostsUseCase: FilterPostsUseCase
): StateScreenModel<PostViewState>(PostViewState.Loading) {

    private var currentPostOffset = 0
    private val test = true

    init {
        coroutineScope.launch {
            if (test) {
                mutableState.value = PostViewState.Success(
                    rawPosts = testPosts,
                    posts = testPosts
                )
                return@launch
            }
            apiRepo.getPost(40, 0)
                .suspendOnSuccess {
                    val posts = it.posts.map(::asPost)
                    mutableState.value = PostViewState.Success(
                        rawPosts = posts,
                        posts = posts
                    )
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
            .suspendOnSuccess { q ->
                mutableState.updateIfSuccess { prev ->
                    val posts = prev.posts.combineWithUnique(q.posts.map(::asPost))
                    PostViewState.Success(
                        rawPosts = posts,
                        posts = filterPostsUseCase(posts, prev.appliedFilters)
                    )
                }
                currentPostOffset += 25
            }
            .suspendOnFailure {
                mutableState.value = PostViewState.Error(it.joinToString())
            }
    }

    fun clearFilters() {
        mutableState.updateIfSuccess {state ->
            state.copy(
                appliedFilters = emptyList(),
                posts = filterPostsUseCase(state.rawPosts, emptyList())
            )
        }
    }
    fun applyFilter(filterable: Filterable) = coroutineScope.launch {
       mutableState.updateIfSuccess { state ->
           val filters = applyPostFiltersUseCase(filterable, state.appliedFilters)
           state.copy(
               appliedFilters = filters,
               posts = filterPostsUseCase(state.rawPosts, filters)
           )
       }
    }

    private fun MutableStateFlow<PostViewState>.updateIfSuccess(
        update: (state: PostViewState.Success) -> PostViewState
    ) {
        this.update {
            update(
                it as? PostViewState.Success ?: return@update it
            )
        }
    }
}



fun List<Post>.combineWithUnique(newList: List<Post>): List<Post> {

    val seen = mutableMapOf<String, Int>()
    val combined = mutableListOf<Post>()

    forEach { post ->
        combined.add(post)
        seen[post.id] = combined.lastIndex
    }

    newList.forEach { post ->
        if(!seen.containsKey(post.id)) {
            combined.add(post)
            seen[post.id] = combined.lastIndex
        } else {
            combined[seen[post.id]!!] = post
        }
    }

    return combined
}