package io.vallfg.valorantlfgmultiplatform.nav

import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.registry.screenModule
import io.vallfg.valorantlfgmultiplatform.PlayerInfo

sealed class SharedScreen : ScreenProvider {
    object PlayerSetup : SharedScreen()
    data class PlayerView(val playerInfo: PlayerInfo) : SharedScreen()
}
