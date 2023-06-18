package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.android.composables.PlayerCard
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PlayerPreviewScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PlayerPreviewState
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

class PlayerPreviewScreen(
    private val players: List<List<String>>
): Screen {

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PlayerPreviewScreenModel>()
        val state by screenModel.state.collectAsState()

        LaunchedEffect(true) {
            screenModel.loadPlayers(players)
        }

        val scope = rememberCoroutineScope()

        val pagerState = rememberPagerState()

        when (state) {
            is PlayerPreviewState.Failure -> {

            }
            PlayerPreviewState.Loading -> {}
            is PlayerPreviewState.Success -> {
                Column {
                    Row(
                        Modifier
                            .height(30.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(state.players.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) Color.Red else Color.LightGray
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable {
                                        scope.launch {
                                            pagerState.animateScrollToPage(iteration)
                                        }
                                    }
                            )
                        }
                    }
                    HorizontalPager(
                        pageCount = state.players.size,
                        state = pagerState,
                    ) { page ->
                        PlayerCard(
                            modifier = Modifier
                                .graphicsLayer {
                                    // Calculate the absolute offset for the current page from the
                                    // scroll position. We use the absolute value which allows us to mirror
                                    // any effects for both directions
                                    val pageOffset = (
                                            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                                            ).absoluteValue

                                    // We animate the alpha, between 50% and 100%
                                    // We animate the scale, between 60% and 100%
                                    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                    alpha = (0.5f * (1.0f - fraction)) + (1f * fraction)
                                    scaleY = (0.6f * (0.8f - fraction)) + (1f * fraction)
                                    scaleX = (0.6f * (0.8f - fraction)) + (1f * fraction)
                                },
                            player = state.players[page]
                        )
                    }
                }
            }
        }
    }
}