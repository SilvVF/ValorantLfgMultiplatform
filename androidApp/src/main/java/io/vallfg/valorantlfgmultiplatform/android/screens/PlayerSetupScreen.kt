package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.android.composables.player_setup.PlayerSetup
import io.vallfg.valorantlfgmultiplatform.screen_models.player_setup.PlayerSetupScreenModel

class PlayerSetupScreen: Screen {


    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PlayerSetupScreenModel>()
        val state by screenModel.state.collectAsState()

        PlayerSetup(
            state = state,
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