package io.vallfg.valorantlfgmultiplatform.android.atoms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.android.R

@Composable
fun TrnScoreIcon(
    modifier: Modifier = Modifier,
    score: Double,
    transform: (AsyncImagePainter.State) -> AsyncImagePainter.State = AsyncImagePainter.DefaultTransform,
    onState: ((AsyncImagePainter.State) -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    val ctx = LocalContext.current
    val icon = remember(score) {
        when (score) {
            in 0.0..299.0 -> "d"
            in 0.0..474.0 -> "c"
            in 0.0..649.0 -> "b"
            in 0.0..824.0 -> "a"
            else -> "s"
        }
    }
    val url by remember(score) {
        derivedStateOf {
            "https://trackercdn.com/cdn/tracker.gg/img/tracker-score/trn-rating-$icon.svg"
        }
    }

    val imageLoader = remember {
        ImageLoader.Builder(ctx)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()
    }
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(ctx)
            .data(url)
            .build(),
        contentDescription = stringResource(id = R.string.trn_score_icon),
        imageLoader = imageLoader,
        transform = transform,
        onState = onState,
        alignment = alignment,
        contentScale = contentScale,
        alpha = alpha,
        colorFilter = colorFilter,
        filterQuality = filterQuality
    )
}