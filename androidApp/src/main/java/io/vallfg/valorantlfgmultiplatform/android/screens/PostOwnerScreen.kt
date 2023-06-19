package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState

class PostOwnerScreen(
    private val minRank: Rank,
    private val gameMode: GameMode,
    private val needed: Int
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostOwnerScreenModel>()
        val state by screenModel.state.collectAsState()


        LaunchedEffect(true) {
            screenModel.connect(minRank, gameMode, needed)
        }

        ContentWithState(state = state)
    }

    @Composable
    private fun ContentWithState(
        state: PostOwnerScreenState
    )  {
        when (state) {
            is PostOwnerScreenState.Failure -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)) {
                    LfgText(text = state.message, modifier = Modifier.align(Alignment.Center))
                }
            }
            PostOwnerScreenState.Loading -> {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Yellow))
            }
            is PostOwnerScreenState.Success -> {
               Box(modifier = Modifier
                   .fillMaxSize()
                   .background(DarkBackGround)) {
                   Column {
                       state.messages.forEach {
                           LfgText(text = it.text)
                       }
                   }
               }
            }
        }
    }
}