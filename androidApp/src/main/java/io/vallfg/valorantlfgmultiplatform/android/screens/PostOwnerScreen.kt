package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.composables.post_owner.JoinedPlayersStats
import io.vallfg.valorantlfgmultiplatform.android.composables.post_owner.rememberJoinedPlayerItems
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenEvent
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState
import kotlinx.coroutines.flow.Flow

class PostOwnerScreen(
    private val minRank: Rank,
    private val gameMode: GameMode,
    private val needed: Int
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostOwnerScreenModel>()
        val state by screenModel.state.collectAsState()
        val events = screenModel.events

        LaunchedEffect(true) {
            screenModel.connect(minRank, gameMode, needed)
        }

        ContentWithState(state = state, events)
    }

    @Composable
    private fun ContentWithState(
        state: PostOwnerScreenState,
        events: Flow<PostOwnerScreenEvent>
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
               PostOwner(state = state, events = events)
            }
            is PostOwnerScreenState.Closed -> Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Magenta))
        }
    }
}


@Composable
fun PostOwner(
    state: PostOwnerScreenState.Success,
    events: Flow<PostOwnerScreenEvent>
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val joinedPlayerItems by rememberJoinedPlayerItems(state = state)

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is PostOwnerScreenEvent.Error -> {
                    snackBarHostState.showSnackbar(
                        message = event.message,
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    LfgScaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            JoinedPlayersStats(
                modifier = Modifier,
                headingToValues = joinedPlayerItems,
                onPlayerInfoIconClick = { idx ->

                }
            )
        }
    }
}


