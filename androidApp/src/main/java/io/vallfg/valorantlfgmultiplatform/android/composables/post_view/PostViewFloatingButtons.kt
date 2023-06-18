package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText

@Composable
fun PostViewFloatingButtons(
    sheetVisible: Boolean,
    showScrollToTop: Boolean,
    onScrollToTopClick: () -> Unit,
    onCreatePostClick: () -> Unit
) {
    Popup(alignment = Alignment.BottomCenter, offset = IntOffset(y = -(32), x= 0)) {
        AnimatedVisibility(
            visible = showScrollToTop && !sheetVisible,
            enter = slideInVertically(animationSpec = tween(300)) { it + 12 },
            exit = slideOutVertically { it },
            modifier = Modifier
                .width(120.dp)
                .height(40.dp)
        ) {
            ExtendedFloatingActionButton(
                onClick = onScrollToTopClick,
                modifier = Modifier
                    .height(40.dp)
                    .offset(y = (-12).dp, x = 0.dp),
                containerColor = Color.Red,
                content = {
                    LfgText(text = "to top")
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "scroll to top",
                        tint = Color.White
                    )
                },
            )
        }
    }

    Popup(alignment = Alignment.BottomEnd, offset = IntOffset(x = -32, y = 0)) {
        AnimatedVisibility(
            visible = !sheetVisible,
            modifier = Modifier.height(40.dp),
            enter = slideInVertically(animationSpec = tween(300)) { it + 12 },
            exit = slideOutVertically { it },
        ) {
            FloatingActionButton(
                onClick = onCreatePostClick,
                modifier = Modifier
                    .height(40.dp)
                    .offset(y = (-12).dp, x = 0.dp),
                containerColor = Color.Red,
                content = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "create post",
                        tint = Color.White
                    )
                }
            )
        }
    }
}