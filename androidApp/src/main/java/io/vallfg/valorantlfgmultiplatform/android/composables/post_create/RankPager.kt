@file:OptIn(ExperimentalFoundationApi::class)

package io.vallfg.valorantlfgmultiplatform.android.composables.post_create

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.Rank
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun RankPager(
    modifier: Modifier = Modifier,
    onRankSelectionChange: (Rank) -> Unit,
) {
    val ctx = LocalContext.current

    val scope = rememberCoroutineScope()

    val pageSize = 200.dp
    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val widthOffset = remember {
        (screenWidthDp / 2f).dp - (pageSize / 2f)
    }
    val ranks = remember {
        Rank.values().map { RankItem(it) }
    }
    val pagerState = rememberPagerState(initialPage = ranks.size / 2)

    val imageLoader = remember {
        ImageLoader.Builder(ctx)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(ctx)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(ctx.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }

    val images = remember {
        mutableStateMapOf<RankItem, Pair<String?, MemoryCache.Key?>>()
    }

    LaunchedEffect(true) {
        // Cache images
        ranks.forEach {
            val result = imageLoader.execute(
                ImageRequest.Builder(ctx)
                    .data(it.imageUrl)
                    .build()
            )
            images[it] = result.request.diskCacheKey to result.request.memoryCacheKey
        }
    }

    LaunchedEffect(true) {
        snapshotFlow { pagerState.currentPage }.collect {
            onRankSelectionChange(ranks[it].rank)
        }
    }

    Column(modifier = modifier) {
        HorizontalPager(
            pageCount = ranks.size,
            state = pagerState,
            contentPadding = PaddingValues(
                horizontal = widthOffset
            ),
            pageSize = PageSize.Fixed(pageSize),
        ) { page ->

            val rankItem = ranks[page]

            Box(
                modifier = Modifier
                    .size(pageSize),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .diskCacheKey(images[rankItem]?.first)
                        .memoryCacheKey(images[rankItem]?.second)
                        .data(rankItem.imageUrl)
                        .crossfade(true)
                        .build(),
                    imageLoader = imageLoader,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(150.dp)
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
                            scaleY = (0.6f * (1.0f - fraction)) + (1f * fraction)
                            scaleX = (0.6f * (1.0f - fraction)) + (1f * fraction)
                        }
                )
            }
        }
        Row(
            Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.Center
        ) {
            repeat(ranks.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.Red else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .aspectRatio(1f)
                        .weight(1f)
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
    }
}