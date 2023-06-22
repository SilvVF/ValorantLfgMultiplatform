package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenEvent
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState
import kotlinx.coroutines.flow.Flow

@Composable
fun PostOwner(
    state: PostOwnerScreenState,
    events: Flow<PostOwnerScreenEvent>
) {
    val snackBarHostState = remember { SnackbarHostState() }

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

    when(state) {
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
            PostOwnerContent(snackBarHostState, state)
        }
        is PostOwnerScreenState.Closed -> Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta)
        )
    }
}

@Composable
fun PostOwnerContent(
    snackbarHostState: SnackbarHostState,
    state: PostOwnerScreenState.Success
) {
    val joinedPlayerItems by rememberJoinedPlayerItems(state = state)

    LfgScaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            JoinedPlayersStats(
                modifier = Modifier.fillMaxWidth(),
                headingToValues = joinedPlayerItems,
                onPlayerInfoIconClick = { idx ->

                }
            )
        }
    }
}
