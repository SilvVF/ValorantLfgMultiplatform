package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import io.vallfg.PostQuery
import io.vallfg.valorantlfgmultiplatform.FilterString
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import kotlin.random.Random

sealed class PostViewState {

    object Loading: PostViewState()

    data class Success(
        val rawPosts: List<Post> = emptyList(),
        val posts: List<Post> = emptyList(),
        val filters: Map<String, List<Filterable>> = mapOf(
            "Game Mode" to GameMode.values(),
            "Min Rank" to Rank.values(),
            "Needed" to (1..4).map { FilterString(it.toString()) }
        ),
        val appliedFilters: List<Filterable> = emptyList()
    ): PostViewState()

    data class Error(val message: String): PostViewState()
}

public data class Post(
    val id: String,
    val players: List<List<String>>,
    val minRank: Rank,
    val minRankIconUrl: String,
    val gameMode: GameMode,
    val needed: Int,
)

fun asPost(p: PostQuery.Post): Post {
    val rank = Rank.fromString(p.minRank)
    return Post(
        id = p.id,
        players = p.players,
        minRank = rank,
        minRankIconUrl = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/${rank.value + 2}.png",
        gameMode = GameMode.fromString(p.gameMode),
        needed = p.needed
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
                    ).random()
                )
            )
        }
    }
}