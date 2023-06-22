package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgBackgroundBox
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.UiMessage

@Composable
fun Messages(
    state: PostOwnerScreenState
) {
    when (state) {
        is PostOwnerScreenState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.messages) { message ->
                    when (message) {
                        is UiMessage.Failed -> {

                        }
                        is UiMessage.Incoming -> {

                        }
                        is UiMessage.Outgoing -> {

                        }
                    }
                }
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