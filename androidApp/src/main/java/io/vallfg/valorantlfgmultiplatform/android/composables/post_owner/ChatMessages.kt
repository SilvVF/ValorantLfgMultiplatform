package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.UiMessage
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun ChatMessages(
    modifier: Modifier,
    messages: List<UiMessage>
) {
    val lazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Box(
        modifier
    ) {
        LazyColumn(
            state = lazyListState
        ) {
            for ((i, uiMessage) in messages.withIndex()) {
                val prevMessage = messages.getOrNull(i - 1)?.message
                val lastMessageBySameUser =
                    prevMessage?.sender?.clientId == uiMessage.message.sender.clientId

                if (!lastMessageBySameUser && prevMessage != null) {
                    item {
                        DateSentSeparator(
                            name = prevMessage.sender.name,
                            tag = prevMessage.sender.tag,
                            sentAtEpochSecond = prevMessage.sentAtEpochSecond
                        )
                    }
                }

                item {
                    when (uiMessage) {
                        is UiMessage.Failed -> {
                            Box(
                                Modifier
                                    .clipMessageShape()
                                    .background(Color.Red)
                                    .padding(12.dp)
                            ) {
                                LfgText(
                                    text = "Failed To Send Message",
                                    fontSize = 22.sp,
                                )
                            }
                        }

                        is UiMessage.Incoming -> {
                            Message(
                                text = uiMessage.message.text,
                                isUserMe = false
                            )
                        }

                        is UiMessage.Outgoing -> {
                            if (uiMessage.loading) {
                                Row(
                                    modifier = Modifier
                                        .clipMessageShape()
                                        .background(
                                            Color.Red
                                        )
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    LfgText(
                                        text = uiMessage.message.text,
                                        fontSize = 22.sp,
                                        modifier = Modifier
                                            .weight(1f, false)
                                            .padding(end = 8.dp)
                                    )
                                    CircularProgressIndicator(
                                        color = Color.White
                                    )
                                }
                            } else {
                                Message(
                                    text = uiMessage.message.text,
                                    isUserMe = true
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        AnimatedVisibility(
            visible = lazyListState.canScrollForward,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(
                    x = (-32).dp,
                    y = (-32).dp
                )
                .size(40.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        lazyListState.animateScrollToItem(
                            lazyListState.layoutInfo.totalItemsCount - 1
                        )
                    }
                },
                containerColor = Color.Red
            ) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "to bottom",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DateSentSeparator(
    name: String,
    tag: String,
    sentAtEpochSecond: Long
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        val dateString by remember {
            derivedStateOf {

                val date = Instant
                    .fromEpochSeconds(sentAtEpochSecond)
                    .toLocalDateTime(
                        TimeZone.currentSystemDefault()
                    )

                "$name #$tag - ${date.hour}:${date.minute}:${date.second}"
            }
        }

        Divider(
            modifier = Modifier
                .weight(1f),
            color = LightBluishGray
        )
        LfgText(text = dateString, modifier = Modifier.padding(12.dp))
        Divider(
            modifier = Modifier.weight(1f),
            color = LightBluishGray
        )
    }
}