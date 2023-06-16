package io.vallfg.valorantlfgmultiplatform.domain.usecase

import io.vallfg.valorantlfgmultiplatform.Needed
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.SortBy
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

/**
 * Filter posts using the given [Filterable] list.
 * If a filter for a category is not provided all values will be allowed.
 * Filters based on the three category's below if any are blank all values in the category are allowed
 *  - [GameMode]
 *  - [Needed]
 *  - [Rank]
 */
class FilterPostsUseCase {

    operator fun invoke(posts: List<Post>, filters: List<Filterable>): List<Post> {

        val rankFilter = filters.find { it is Rank }
        val stringFilter = filters.find { it is Needed }
        val gameModeFilter = filters.find { it is GameMode }
        val sortBy = filters.find { it is SortBy }

        val filtered = posts.filter { post ->
                rankFilter?.run { this.string == post.minRank.string } ?: true &&
                stringFilter?.run { this.string == post.needed.toString() } ?: true &&
                gameModeFilter?.run { this.string == post.gameMode.string } ?: true
        }
        return when (sortBy) {
                is SortBy.HighestRank -> filtered.sortedByDescending { it.minRank.value }
                is SortBy.LowestRank -> filtered.sortedBy { it.minRank.value }
                is SortBy.LeastPlayers -> filtered.sortedBy { it.players.size }
                is SortBy.MostPlayers -> filtered.sortedByDescending { it.players.size }
                is SortBy.Newest -> filtered.sortedByDescending { it.date.toInstant(TimeZone.currentSystemDefault()).epochSeconds }
                is SortBy.Oldest -> filtered.sortedBy { it.date.toInstant(TimeZone.currentSystemDefault()).epochSeconds }
                else -> filtered
        }
    }
}