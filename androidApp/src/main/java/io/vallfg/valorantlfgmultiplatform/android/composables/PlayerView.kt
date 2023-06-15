package io.vallfg.valorantlfgmultiplatform.android.composables

import android.content.res.Resources
import android.util.TypedValue
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.android.theme.ValFont
import kotlinx.coroutines.launch

val Number.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics
)



@Composable
fun PlayerView(
    player: PlayerInfo,
    navigate: () -> Unit,
    navigateBack: () -> Unit
) {
    val scope = rememberCoroutineScope()

    BackHandler {

    }

    var currentOffsetPx by remember {
        mutableStateOf(0f)
    }

    val cardRotation = remember {
        androidx.compose.animation.core.Animatable(initialValue = 0f,)
    }

    val cardRotationPassedNavigationLine by remember(cardRotation) {
        derivedStateOf {
            when {
                cardRotation.value > 20f -> true
                cardRotation.value < -20f -> false
                else -> null
            }
        }
    }
    LaunchedEffect(cardRotationPassedNavigationLine) {
        when (cardRotationPassedNavigationLine) {
            true -> {
                navigate()
            }
            false -> navigateBack()
            else -> Unit
        }
    }

    val offsetX by remember(cardRotation) {
        derivedStateOf { cardRotation.value * 15f }
    }

    val offsetY by remember(offsetX) {
        derivedStateOf { offsetX * (1 / 20 * offsetX) }
    }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val dragState = rememberDraggableState { deltaInPx ->
        scope.launch {
            currentOffsetPx += deltaInPx
            cardRotation.snapTo((currentOffsetPx / (screenWidthDp.toPx)) * 30f)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .draggable(
                dragState,
                Orientation.Horizontal,
                onDragStarted = {
                       cardRotation.stop()
                },
                onDragStopped = {
                    currentOffsetPx = 0f
                    cardRotation.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                }
            )
            .systemBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var visible by rememberSaveable {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit) {
            visible = true
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically { -it }
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                if (cardRotation.value > 0) {
                    Text(
                        text = "Confirm",
                        color = Color.Red,
                        fontSize = (32f * (cardRotation.value / 20f)).sp,
                        fontFamily = ValFont,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = (40).dp)

                    )
                } else {
                    Text(
                        text = "Go Back",
                        color = Color.Red,
                        fontSize = -(32f * (cardRotation.value / 25f)).sp,
                        fontFamily = ValFont,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = (-40).dp)
                    )
                }
                PlayerCard(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .rotate(cardRotation.value)
                        .offset(
                            x = offsetX.dp,
                            y = offsetY.dp
                        )
                        .offset(
                            x = when (cardRotationPassedNavigationLine) {
                                true -> animateDpAsState(targetValue = 1500.dp).value
                                false -> animateDpAsState(targetValue = (-1500).dp).value
                                else -> 0.dp
                            },
                            y = when (cardRotationPassedNavigationLine) {
                                true, false -> animateDpAsState(targetValue = 100.dp).value
                                else -> 0.dp
                            }
                        ),
                    player = player
                )
            }
        }
    }
}


