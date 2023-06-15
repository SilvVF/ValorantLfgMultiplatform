package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import io.vallfg.PostQuery
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import kotlin.math.min

sealed class PostViewState {

    object Loading: PostViewState()

    data class Success(
        val posts: List<Post>
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