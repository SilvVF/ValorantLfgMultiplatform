package io.vallfg.valorantlfgmultiplatform.domain.usecase

import io.vallfg.valorantlfgmultiplatform.FilterString
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post

class FilterPostsUseCase {

    operator fun invoke(posts: List<Post>, filters: List<Filterable>): List<Post> {

        val rankFilter = filters.find { it is Rank }
        val stringFilter = filters.find { it is FilterString }
        val gameModeFilter = filters.find { it is GameMode }

        return posts.filter { post ->
            buildList {
                rankFilter?.let { filter -> add(post.minRank.string == filter.string) } ?: add(true)
                stringFilter?.let { filter -> add(post.needed.toString() == filter.string) } ?: add(true)
                gameModeFilter?.let { filter -> add(post.gameMode.string == filter.string) } ?: add(true)
            }
                .all { it }
        }
    }
}