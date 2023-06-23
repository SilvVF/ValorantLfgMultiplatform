package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgBackgroundBox
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState

fun Modifier.clipMessageShape() = this.clip(
    RoundedCornerShape(
        topStart = 6.dp,
        topEnd = 18.dp,
        bottomEnd = 18.dp,
        bottomStart = 18.dp
    )
)

@Composable
fun Messages(
    state: PostOwnerScreenState,
) {
    Column(Modifier.fillMaxSize()) {
        when (state) {
            is PostOwnerScreenState.Success -> {
                ChatMessages(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    messages = state.messages
                )
                TextInput { message ->
                    
                }
            }
            else -> {
                LfgBackgroundBox(
                    modifier = Modifier.fillMaxSize()
                ) {
                    LfgText(text = "loading messages", fontSize = 42.sp)
                }
            }
        }
    }
}




@Composable
fun Message(
    modifier: Modifier = Modifier,
    text: String,
    isUserMe: Boolean,
) {
    Box(
        modifier = modifier
            .clipMessageShape()
            .background(
                if (isUserMe)
                    Color.Red
                else
                    LightBluishGray
            )
            .padding(12.dp)
    ) {
        LfgText(
            text = text,
            fontSize = 22.sp,
        )
    }
}