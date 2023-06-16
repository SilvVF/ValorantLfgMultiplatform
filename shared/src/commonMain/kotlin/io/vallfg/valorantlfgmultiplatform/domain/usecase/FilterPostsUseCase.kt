package io.vallfg.valorantlfgmultiplatform.domain.usecase

import io.vallfg.valorantlfgmultiplatform.FilterString
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post

/**
 * Filter posts using the given [Filterable] list.
 * If a filter for a category is not provided all values will be allowed.
 * Filters based on the three category's below if any are blank all values in the category are allowed
 *  - [GameMode]
 *  - [FilterString]
 *  - [Rank]
 */
class FilterPostsUseCase {

    operator fun invoke(posts: List<Post>, filters: List<Filterable>): List<Post> {

        val rankFilter = filters.find { it is Rank }
        val stringFilter = filters.find { it is FilterString }
        val gameModeFilter = filters.find { it is GameMode }

        return posts.filter { post ->
                rankFilter?.run { this.string == post.minRank.string } ?: true &&
                stringFilter?.run { this.string == post.needed.toString() } ?: true &&
                gameModeFilter?.run { this.string == post.gameMode.string } ?: true
        }
    }
}