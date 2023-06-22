package io.vallfg.valorantlfgmultiplatform.android.atoms

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.atoms.Screen.LEFT
import io.vallfg.valorantlfgmultiplatform.android.atoms.Screen.MID
import io.vallfg.valorantlfgmultiplatform.android.atoms.Screen.RIGHT
import kotlinx.coroutines.launch

private enum class Screen {
    LEFT, MID, RIGHT
}

@Composable
fun ThreePaneLayout(
    left: @Composable () -> Unit,
    middle: @Composable () -> Unit,
    right: @Composable () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    var currentScreen by remember {
        mutableStateOf(MID)
    }

    val midToLeftThreshold = remember {
         screenWidth / 2f
    }
    val midToRightThreshold = remember {
        -(screenWidth / 2f)
    }

    val leftToMidThreshold = remember {
        screenWidth - midToLeftThreshold
    }

    val rightToMidThreshold = remember {
       -screenWidth + midToLeftThreshold
    }

    val isRight by remember {
        derivedStateOf { currentScreen == RIGHT }
    }

    val isMiddle by remember {
        derivedStateOf { currentScreen == MID }
    }

    val isLeft by remember {
        derivedStateOf { currentScreen == LEFT }
    }

    val middleOffset = remember {
        Animatable(
            initialValue = 0f
        )
    }

    val leftOffset = remember {
        Animatable(
            initialValue = -screenWidth.dp.value
        )
    }
    val rightOffset = remember {
        Animatable(
            initialValue = screenWidth.dp.value
        )
    }

    var dragOffset by remember {
        mutableStateOf(0f)
    }

    val middleDrag = rememberDraggableState { delta ->
        val drag = (middleOffset.value + delta)
            .coerceIn(-screenWidth.dp.value, screenWidth.dp.value)
        dragOffset = drag
    }

    val leftDrag = rememberDraggableState { delta ->
        val drag = (middleOffset.value + delta)
            .coerceIn(-screenWidth.dp.value, screenWidth.dp.value)
        dragOffset = drag
    }


    val rightDrag = rememberDraggableState { delta ->
        val drag = (middleOffset.value + delta)
            .coerceIn(-screenWidth.dp.value, screenWidth.dp.value)
        dragOffset = drag
    }

    LaunchedEffect(Unit) {
        snapshotFlow { dragOffset }.collect {
            launch {
                middleOffset.animateTo(it)
            }
            launch {
                leftOffset.animateTo(
                    -screenWidth + it
                )
            }
            launch {
                rightOffset.animateTo(
                    screenWidth + it
                )
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .offset(x = leftOffset.value.dp)
        .composed {
            if (isLeft) {
                return@composed this.draggable(leftDrag, Orientation.Horizontal,
                    onDragStopped = {
                        if (dragOffset < leftToMidThreshold) {
                            dragOffset = 0f
                            currentScreen = MID
                        } else {
                            dragOffset = screenWidth.dp.value
                        }
                    }
                )
            }
            this
        }
    ) {
        left()
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .offset(x = middleOffset.value.dp)
        .composed {
            if (isMiddle) {
                return@composed this.draggable(middleDrag, Orientation.Horizontal,
                    onDragStopped = {
                        if (dragOffset > midToLeftThreshold) {
                            dragOffset = screenWidth.dp.value
                            currentScreen = LEFT
                        } else if (dragOffset < midToRightThreshold) {
                            dragOffset = -screenWidth.dp.value
                            currentScreen = RIGHT
                        } else {
                            dragOffset = 0f
                        }
                    }
                )
            }
            this
        }
    ) {
        middle()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset(x = rightOffset.value.dp)
            .composed {
                if (isRight) {
                    return@composed this.draggable(rightDrag, Orientation.Horizontal,
                        onDragStopped = {
                            if (dragOffset > rightToMidThreshold) {
                                dragOffset = 0f
                                currentScreen = MID
                            } else {
                                dragOffset = -screenWidth.dp.value
                            }
                        }
                    )
                }
                this
            }
    ) {
        right()
    }
}


@Preview
@Composable
fun ThreePaneLayoutPreview() {
    ThreePaneLayout(
        left = {
               Box(modifier = Modifier
                   .fillMaxSize()
                   .background(Color.Green),
                   contentAlignment = Alignment.Center
               ) {
                   Text(text = "LEFT", fontSize = 42.sp)
               }
        },
        right = {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "RIGHT", fontSize = 42.sp)
            }
        },
        middle = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "MIDDLE", fontSize = 42.sp)
            }
        }
    )
}