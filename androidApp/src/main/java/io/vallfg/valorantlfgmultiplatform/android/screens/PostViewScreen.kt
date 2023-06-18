@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)

package io.vallfg.valorantlfgmultiplatform.android.screens


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.Filterable
import io.vallfg.valorantlfgmultiplatform.android.composables.post_view.PostView
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewState

data class PostViewScreenProps(
    val name: String,
    val tag: String
)

class PostViewScreen(
    private val props: PostViewScreenProps,
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostViewScreenModel>()
        val state by screenModel.state.collectAsState()
        
        when (state) {
            is PostViewState.Success -> {
                PostView(
                    posts = state.posts,
                    applyFilter = { filter: Filterable ->
                        screenModel.applyFilter(filter)
                    },
                    filters = state.filters,
                    appliedFilters = state.appliedFilters,
                    removeAllFilters = {
                        screenModel.clearFilters()
                    }
                )
            }
            else -> {
                Surface(Modifier.fillMaxSize()) {

                }
            }
        }
    }
}