package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray

@Composable
fun PlayerIcon(
    playerName: List<String>?
) {
    if (playerName != null) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color.Red)
        ) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Player",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(LightGray)
                .size(24.dp)
        )
    }
}