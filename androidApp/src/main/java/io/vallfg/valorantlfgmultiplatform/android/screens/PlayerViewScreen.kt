package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.android.composables.PlayerView


class PlayerViewScreen(private val player: PlayerInfo): Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current ?: return


        PlayerView(
            player = player,
            navigate = {
                navigator.push(
                    PostViewScreen(
                        PostViewScreenProps("silv", "004")
                    )
                )
            },
            navigateBack = {
                navigator.pop()
            }
        )
    }
}