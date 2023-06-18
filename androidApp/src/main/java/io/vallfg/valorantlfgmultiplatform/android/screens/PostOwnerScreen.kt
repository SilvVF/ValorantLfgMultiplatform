package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank

class PostOwnerScreen(
    val minRank: Rank,
    val gameMode: GameMode,
    val needed: Int
): Screen {

    @Composable
    override fun Content() {

    }
}