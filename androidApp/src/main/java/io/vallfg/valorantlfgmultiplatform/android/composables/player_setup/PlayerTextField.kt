@file:OptIn(ExperimentalComposeUiApi::class)
package io.vallfg.valorantlfgmultiplatform.android.composables.player_setup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.R

class PlayerTextFieldState(
    private val focusManager: FocusManager,
    private val keyboard: SoftwareKeyboardController?,
    val keyboardOptions: KeyboardOptions,
    val keyboardActions: KeyboardActions,
    val searchingAnimationSpec: InfiniteRepeatableSpec<Float>,
    val searching: Boolean,
    val singleLine: Boolean,
) {

    var focused by mutableStateOf(false)

    @OptIn(ExperimentalComposeUiApi::class)
    fun hideKeyboardAndClearFocus() {
        keyboard?.hide()
        focusManager.clearFocus()
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun rememberPlayerTextFieldState(
    focusManager: FocusManager = LocalFocusManager.current,
    keyboard: SoftwareKeyboardController? = LocalSoftwareKeyboardController.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    searchingAnimationSpec: InfiniteRepeatableSpec<Float> = InfiniteRepeatableSpec(
        animation = tween(700, easing = FastOutSlowInEasing)
    ),
    singleLine: Boolean = true,
    searching: Boolean = false,
) = remember {
    PlayerTextFieldState(focusManager, keyboard, keyboardOptions, keyboardActions, searchingAnimationSpec, searching, singleLine)
}

@Composable
fun PlayerTextField(
    modifier: Modifier = Modifier,
    text: String,
    hint: String =  stringResource(
        id = R.string.player_text_field_hint
    ),
    playerTextFieldState: PlayerTextFieldState,
    onTextChanged: (String) -> Unit,
) {

    val darkGrayLogoBg = Color(0xff1A2733)

    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                playerTextFieldState.focused = it.isFocused
            },
        value = text,
        textStyle = TextStyle(fontSize = 24.sp, color = Color.White),
        onValueChange = onTextChanged,
        singleLine = playerTextFieldState.singleLine,
        cursorBrush = SolidColor(Color.Red),
        keyboardOptions = playerTextFieldState.keyboardOptions,
        keyboardActions = playerTextFieldState.keyboardActions
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(12.dp)
                )
                .border(
                    1.dp,
                    Color.Black,
                    RoundedCornerShape(12.dp)
                )
                .drawBehind {
                    drawRect(
                        color = darkGrayLogoBg,
                        topLeft = Offset.Zero,
                        size = Size(
                            width = this.size.width * 0.2f,
                            height = this.size.height
                        )
                    )
                }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                Modifier.fillMaxWidth(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.valorant_logo_small_white),
                    contentDescription = stringResource(id = R.string.valorant_logo),
                    modifier = Modifier
                        .padding(8.dp)
                        .rotate(
                            if (playerTextFieldState.searching) {
                                rememberInfiniteTransition()
                                    .animateFloat(
                                        initialValue = 0f,
                                        targetValue = 360f,
                                        animationSpec = playerTextFieldState.searchingAnimationSpec
                                    ).value
                            } else {
                                0f
                            }
                        ),
                    tint = Color.White
                )
                AnimatedVisibility(visible = playerTextFieldState.focused) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(2.dp)
                            .background(Color.Red)
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                innerTextField()
                text.ifEmpty {
                    Text(
                        text = hint,
                        color = Color.LightGray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}