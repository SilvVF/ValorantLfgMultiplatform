package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import io.vallfg.valorantlfgmultiplatform.android.composables.post_create.PostCreate

class PostCreateScreen: Screen {

    @Composable
    override fun Content() {

        val navigator = LocalNavigator.current

        PostCreate(
           navigateBack = {
               navigator?.pop()
           } 
        ) { rank, gameMode, needed ->  
           navigator?.replace(
               PostOwnerScreen(rank, gameMode, needed)
           )
        }
    }
}