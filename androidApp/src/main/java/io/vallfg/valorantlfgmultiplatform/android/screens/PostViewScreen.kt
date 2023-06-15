package io.vallfg.valorantlfgmultiplatform.android.screens


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewState

data class PostViewScreenProps(
    val name: String,
    val tag: String
)

class PostViewScreen(
    private val props: PostViewScreenProps
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostViewScreenModel>()

        val state = screenModel.state.collectAsState().value
        
        when (state) {
            is PostViewState.Success -> {
                LazyColumn(
                    Modifier.fillMaxSize()
                ) {
                    items(
                        items = state.posts,
                        key = { post -> post.id }
                    ) {
                        Text(text = it.toString())
                    }
                }
            }
            else -> {
                
            }
        }
        
    }
}