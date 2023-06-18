package io.vallfg.valorantlfgmultiplatform.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import io.vallfg.valorantlfgmultiplatform.PlayerInfo

/**
 * [ScreenProvider] allows all modules to access registered screens.
 *  use ScreenRegistry to get the screen or rememberScreen from compose.
 */
sealed class SharedScreen : ScreenProvider {
    object PlayerSetup : SharedScreen()
    data class PlayerView(val playerInfo: PlayerInfo) : SharedScreen()

    data class PostView(val name: String, val tag: String): SharedScreen()

    object PostCreate: SharedScreen()
}
