package io.vallfg.valorantlfgmultiplatform.android

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import io.vallfg.valorantlfgmultiplatform.android.theme.ValorantLfgTheme
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.nav.SharedScreen
import org.koin.androidx.compose.get

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            val playerSetupScreen = rememberScreen(SharedScreen.PlayerSetup)
            val lfgNavigator = get<DestinationsNavigator>()

            ValorantLfgTheme {
                Navigator(playerSetupScreen) {

                    LaunchedEffect(key1 = true) {
                        lfgNavigator.handleNavigationCommands(it)
                    }

                    FadeTransition(navigator = it)
                }
            }
        }
    }
}
