package io.vallfg.valorantlfgmultiplatform.android.composables.post_create

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText

@Composable
fun LfgSelector(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedIdx: Int,
    boxColor: Color = Color.Red,
    lineColor: Color = Color.White,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 200,
        easing = FastOutSlowInEasing
    ),
    onSelectionChanged: (Int) -> Unit,
) {

    var boxSize by remember {
        mutableStateOf(0f)
    }

    val boxOffset = remember {
        Animatable(selectedIdx * boxSize)
    }

    LaunchedEffect(selectedIdx) {
        boxOffset.animateTo(
            targetValue = selectedIdx * boxSize,
            animationSpec = animationSpec
        )
    }

    Row(
        modifier = modifier
            .drawBehind {

                boxSize = size.width / items.size

                drawRoundRect(
                    color = boxColor,
                    topLeft = Offset(y = 0f, x = boxOffset.value),
                    size = Size(width = boxSize, height = this.size.height)
                )

            }
            .drawWithCache {
                onDrawBehind {
                    for (i in items.indices) {
                        if (i != items.lastIndex) {
                            drawLine(
                                color = lineColor,
                                start = Offset(x = boxSize * (i + 1), y = this.size.height),
                                end = Offset(x = boxSize * (i + 1), y = 0f),
                                strokeWidth = 4f
                            )
                        }
                    }
                }
            }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,

    ) {
        for ((i, mode) in items.withIndex()) {
            LfgText(
                text = mode,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        }
                    ) {
                        onSelectionChanged(i)
                    }
            )
        }
    }
}