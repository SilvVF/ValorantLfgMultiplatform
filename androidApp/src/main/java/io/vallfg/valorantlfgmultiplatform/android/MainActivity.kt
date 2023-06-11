package io.vallfg.valorantlfgmultiplatform.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import io.vallfg.valorantlfgmultiplatform.android.screens.RegisterDestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.android.theme.ValorantLfgTheme
import io.vallfg.valorantlfgmultiplatform.nav.SharedScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val scope = rememberCoroutineScope()
            val playerSetupScreen = rememberScreen(SharedScreen.PlayerSetup)

            ValorantLfgTheme {
                Navigator(
                    RegisterDestinationsNavigator(playerSetupScreen)
                )
            }
        }
    }
}
