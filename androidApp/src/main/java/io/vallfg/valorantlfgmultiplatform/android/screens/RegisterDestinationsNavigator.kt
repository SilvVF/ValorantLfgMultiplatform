package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import org.koin.androidx.compose.get

class RegisterDestinationsNavigator(
   private val start: Screen
): Screen {

    @Composable
    override fun Content() {

        val nav = get<DestinationsNavigator>()

        val navigator = LocalNavigator.current ?: return

        LaunchedEffect(key1 = true) {
            nav.handleNavigationCommands(navigator)
        }

        start.Content()
    }
}