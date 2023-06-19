package io.vallfg.valorantlfgmultiplatform.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.Rank

/**
 * [ScreenProvider] allows all modules to access registered screens.
 *  use ScreenRegistry to get the screen or rememberScreen from compose.
 */
sealed class SharedScreen : ScreenProvider {
    object PlayerSetup : SharedScreen()
    data class PlayerView(val playerInfo: PlayerInfo) : SharedScreen()

    data class PostView(val name: String, val tag: String): SharedScreen()

    object PostCreate: SharedScreen()

    data class PostOwner(
        val minRank: Rank,
        val gameMode: GameMode,
        val needed: Int,
    ): SharedScreen()
}
