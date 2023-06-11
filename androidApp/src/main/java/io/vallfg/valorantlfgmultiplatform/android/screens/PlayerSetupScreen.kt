package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import io.vallfg.valorantlfgmultiplatform.android.composables.PlayerSetup
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.screen_models.player_setup.PlayerSetupScreenModel
import org.koin.androidx.compose.get
import org.koin.androidx.compose.inject

class PlayerSetupScreen: Screen {



    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PlayerSetupScreenModel>()
        val state by screenModel.state.collectAsState()

        PlayerSetup(
            playerSetupState = state,
            errors = screenModel.errorChannel,
            onPlayerAccountChange = { account ->
                screenModel.handleAccountChanged(account)
            },
            onGetPlayerButtonClick = { account ->
                screenModel.handleSubmitAccount(account)
            }
        )
    }
}