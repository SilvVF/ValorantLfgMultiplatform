package io.vallfg.valorantlfgmultiplatform.screen_models.post_owner

import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import kotlinx.datetime.LocalDateTime

sealed class PostOwnerScreenState(
    open val clientId: String = "",
    open val users: Map<String, WsPlayerData> = emptyMap(),
) {

    object Loading: PostOwnerScreenState()

    data class Success(
        val postId: String? = null,
        override val clientId: String = "",
        val messages: List<UiMessage> = emptyList(),
        override val users: Map<String, WsPlayerData> = emptyMap(),
        val postState: PostState? = null,
    ): PostOwnerScreenState()

    data class Closed(
        val messages: List<Message> = emptyList(),
        override val users: Map<String, WsPlayerData> = emptyMap(),
        val postState: PostState,
    ) : PostOwnerScreenState()

    data class Failure(
        val message: String
    ): PostOwnerScreenState()
}

sealed class UiMessage {

    data class Outgoing(
        val id: Int,
        val message: Message,
        val loading: Boolean,
        val sentAt: LocalDateTime
    ): UiMessage()

    data class Failed(val text: String): UiMessage()

    data class Incoming(
        val message: Message,
    ): UiMessage()
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