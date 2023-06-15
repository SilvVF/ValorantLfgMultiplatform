package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.TopAppBarStyle
import io.vallfg.valorantlfgmultiplatform.android.composables.PlayerView


class PlayerViewScreen(private val player: PlayerInfo): Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current ?: return

        LfgScaffold(
            topAppBarStyle = TopAppBarStyle(
                title = {
                    Text(
                        text = "Swipe Right to confirm Left to go back.",
                        color =  Color(0xff0F1923),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            )
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                PlayerView(
                    player = player,
                    navigate = {
                        navigator.replaceAll(
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
    }
}