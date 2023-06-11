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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.silv.valorantlfg.ui.composables.PlayerCard
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.android.theme.ValFont
import kotlinx.coroutines.launch
import kotlin.math.abs

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

    val offsetY by remember(cardRotation) {
        derivedStateOf { -abs(cardRotation.value * 3f) }
    }

    val screenWidthDp = LocalConfiguration.current.screenWidthDp

    val dragState = rememberDraggableState { deltaInPx ->
        scope.launch {
            currentOffsetPx += deltaInPx
            cardRotation.animateTo(currentOffsetPx / (screenWidthDp.toPx) * 30f)
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
                    cardRotation.animateTo(
                        targetValue = 0f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    currentOffsetPx = 0f
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
        Spacer(modifier = Modifier.height(64.dp))
        Text(
            text = "Swipe Right to confirm Left to go back.",
            color =  Color(0xff0F1923),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
        )
        Spacer(modifier = Modifier.height(32.dp))
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
                        fontSize = 22.sp,
                        fontFamily = ValFont,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .offset(x = (40).dp)
                            .graphicsLayer {
                                val scale = cardRotation.value * 0.1f
                                scaleX = scale
                                scaleY = scale
                            }
                    )
                } else {
                    Text(
                        text = "Go Back",
                        color = Color.Red,
                        fontSize = 22.sp,
                        fontFamily = ValFont,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = (-40).dp)
                            .graphicsLayer {
                                val scale = cardRotation.value * -0.1f
                                scaleX = scale
                                scaleY = scale
                            }
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


