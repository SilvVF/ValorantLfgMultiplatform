package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import io.vallfg.PostQuery
import io.vallfg.valorantlfgmultiplatform.Needed
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.SortBy
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToLong
import kotlin.random.Random

sealed class PostViewState(
    open val rawPosts: List<Post> = emptyList(),
    open val posts: List<Post> = emptyList(),
    val filters: Map<String, List<Filterable>> = mapOf(
        "Game Mode" to GameMode.values(),
        "Min Rank" to Rank.values(),
        "Needed" to (1..4).map { Needed(it.toString()) },
        "Sort by" to SortBy.values()
    ),
    open val appliedFilters: List<Filterable> = emptyList()
) {

    object Loading: PostViewState()

    data class Success(
        override val posts: List<Post> = emptyList(),
        override val rawPosts: List<Post> = emptyList(),
        override val appliedFilters: List<Filterable> = emptyList(),
    ): PostViewState(
        posts = posts,
        rawPosts = rawPosts,
        appliedFilters = appliedFilters
    )

    data class Error(val message: String): PostViewState()
}

data class Post(
    val id: String,
    val players: List<List<String>>,
    val minRank: Rank,
    val minRankIconUrl: String,
    val gameMode: GameMode,
    val needed: Int,
    val date: LocalDateTime
)

fun asPost(p: PostQuery.Post): Post {
    val rank = Rank.fromString(p.minRank)
    return Post(
        id = p.id,
        players = p.players,
        minRank = rank,
        minRankIconUrl = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/${rank.value + 2}.png",
        gameMode = GameMode.fromString(p.gameMode),
        needed = p.needed,
        date = Instant
            .fromEpochSeconds(p.createdAtEpochSecond.roundToLong())
            .toLocalDateTime(TimeZone.currentSystemDefault())
    )
}

val testPosts by lazy(LazyThreadSafetyMode.NONE) {
    buildList {
        repeat(40) {
            val rank =
                listOf(Rank.Diamond1, Rank.Immortal1, Rank.Iron2, Rank.Plat2, Rank.Gold3).random()
            add(
                Post(
                    id = Random.nextInt().toString(),
                    players = listOf(
                        listOf("silv", "004"),
                    ),
                    needed = (1..4).random(),
                    minRank = rank,
                    minRankIconUrl = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/${rank.value + 2}.png",
                    gameMode = listOf(
                        GameMode.Competitive,
                        GameMode.Unrated,
                        GameMode.SpikeRush
                    ).random(),
                    date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
            )
        }
    }
}