package io.vallfg.valorantlfgmultiplatform.android.screens


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel

data class PostViewScreenProps(
    val name: String,
    val tag: String
)

class PostViewScreen(
    private val props: PostViewScreenProps
): Screen {

    @Composable
    override fun Content() {

    }
}