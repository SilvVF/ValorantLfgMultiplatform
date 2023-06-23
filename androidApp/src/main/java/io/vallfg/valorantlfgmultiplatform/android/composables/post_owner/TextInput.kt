@file:OptIn(ExperimentalComposeUiApi::class)

package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.vallfg.valorantlfgmultiplatform.android.composables.player_setup.PlayerTextField
import io.vallfg.valorantlfgmultiplatform.android.composables.player_setup.rememberPlayerTextFieldState
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray

@Composable
fun TextInput(
    onSend: (message: String) -> Unit
) {
    var chatMessage by rememberSaveable {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(LightBluishGray)
            .padding(4.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PlayerTextField(
            text = chatMessage,
            onTextChanged = { chatMessage = it },
            hint = "send a message",
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .imePadding(),
            playerTextFieldState = rememberPlayerTextFieldState(
                singleLine = false,
                keyboardActions = KeyboardActions(
                    onSend = {
                        onSend(chatMessage)
                        chatMessage = ""
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                )
            )
        )
        IconButton(
            onClick = {
                onSend(chatMessage)
                chatMessage = ""
            },
            enabled = chatMessage.isNotBlank(),
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowCircleUp,
                contentDescription = "send",
                tint = if (chatMessage.isNotBlank()) {
                    Color.Red
                } else {
                    DarkBluishBlack
                }
            )
        }
    }
}