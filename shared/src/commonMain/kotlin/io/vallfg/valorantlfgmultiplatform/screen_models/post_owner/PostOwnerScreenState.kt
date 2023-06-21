package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData

sealed class PostOwnerScreenState {

    object Loading: PostOwnerScreenState()

    data class Success(
        val messages: List<Message> = emptyList(),
        val users: Map<String, WsPlayerData> = emptyMap(),
        val postState: PostState? = null,
    ): PostOwnerScreenState()

    data class Failure(
        val message: String
    ): PostOwnerScreenState()
}

data class PostState(
    val creator: WsPlayerData = WsPlayerData.emptyPlayer,
    val banned: List<WsPlayerData> = emptyList(),
    val minRank: Rank = Rank.Unranked,
    val needed: Int = 0,
    val gameMode: GameMode = GameMode.Competitive
)

sealed interface PostOwnerScreenEvent {
    data class Error(val message: String): PostOwnerScreenEvent
}