package io.silv.valorantlfg.ui.composables

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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.R


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PlayerTextField(
    modifier: Modifier = Modifier,
    searching: Boolean = false,
    text: String,
    onTextChanged: (String) -> Unit,
    onDone: () -> Unit
) {

    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var focused by remember { mutableStateOf(false) }
    val darkGrayLogoBg = Color(0xff1A2733)

    BasicTextField(
        modifier = modifier
            .onFocusChanged {
                focused = it.isFocused
            },
        value = text,
        textStyle = TextStyle(fontSize = 24.sp),
        onValueChange = onTextChanged,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboard?.hide()
                focusManager.clearFocus()
                onDone()
            }
        )
    ) { innerTextField ->
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                Modifier
                    .weight(0.2f, false)
                    .fillMaxHeight()
                    .background(darkGrayLogoBg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.valorant_logo_small_white),
                    contentDescription = stringResource(id = R.string.valorant_logo),
                    modifier = Modifier
                        .padding(8.dp)
                        .rotate(
                            if (searching) {
                                rememberInfiniteTransition()
                                    .animateFloat(
                                        initialValue = 0f,
                                        targetValue = 360f,
                                        animationSpec = InfiniteRepeatableSpec(
                                            animation = tween(700, easing = FastOutSlowInEasing)
                                        )
                                    ).value
                            } else {
                               0f
                            }
                        ),
                    tint = Color.White
                )
                AnimatedVisibility(visible = focused) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(2.dp)
                            .background(Color.Red)
                    )
                }
            }
            Box(
                Modifier
                    .weight(0.8f)
                    .padding(start = 12.dp), contentAlignment = Alignment.CenterStart) {
                innerTextField()
                text.ifEmpty {
                    Text(
                        text = stringResource(id = R.string.player_text_field_hint),
                        color = Color.LightGray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}