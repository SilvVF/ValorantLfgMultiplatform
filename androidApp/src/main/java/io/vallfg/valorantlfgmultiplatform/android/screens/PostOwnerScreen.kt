package io.vallfg.valorantlfgmultiplatform.android.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgBackgroundBox
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.atoms.ThreePaneLayout
import io.vallfg.valorantlfgmultiplatform.android.composables.post_owner.Messages
import io.vallfg.valorantlfgmultiplatform.android.composables.post_owner.PostInfo
import io.vallfg.valorantlfgmultiplatform.android.composables.post_owner.PostOwner
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBackGround
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.UiMessage
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PostOwnerScreen(
    private val minRank: Rank,
    private val gameMode: GameMode,
    private val needed: Int
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostOwnerScreenModel>()
        val state by screenModel.state.collectAsState()
        val events = screenModel.events

        LaunchedEffect(true) {
            screenModel.connect(minRank, gameMode, needed)
        }

        ThreePaneLayout(
            left = {

            },
            right = {
                Messages(state)
            },
            middle = {
                PostOwner(state = state, events)
            }
        )
    }
}

@Preview
@Composable
fun PostOwnerSreenPreview() {

    val state = remember {
        PostOwnerScreenState.Success(
            postId = "12345",
            clientId = "123",
            users = buildMap {
                put("123", WsPlayerData.emptyPlayer)
                repeat(3) {
                    put(it.toString(), WsPlayerData.emptyPlayer)
                }
            },
            messages = buildList {
                repeat(10) {
                    add(UiMessage.Incoming(
                        message = Message(
                            text = "Message #$it",
                            sendId = 1213,
                            sender = WsPlayerData.emptyPlayer.copy(name = "user${it % 3}", clientId = (it % 3).toString()),
                            sentAtEpochSecond = Clock.System.now().epochSeconds
                        )
                    ))
                }
                add(UiMessage.Failed(
                    message = Message(
                        text = "Message #Send",
                        sendId = 1213,
                        sender = WsPlayerData.emptyPlayer.copy(name = "me", clientId = "123"),
                        sentAtEpochSecond = Clock.System.now().epochSeconds
                    )
                ))
                add(
                    UiMessage.Outgoing(
                        loading = true,
                        message = Message(
                            text = "Message #Send",
                            sendId = 1213,
                            sender = WsPlayerData.emptyPlayer.copy(name = "me", clientId = "123"),
                            sentAtEpochSecond = Clock.System.now().epochSeconds
                        ),
                        id = 0,
                        sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    )
                )
                repeat(3) {
                    add(UiMessage.Outgoing(
                        loading = false,
                        message = Message(
                            text = "Message #Send $it",
                            sendId = 1213,
                            sender = WsPlayerData.emptyPlayer.copy(name = "me", clientId = "123"),
                            sentAtEpochSecond = Clock.System.now().epochSeconds
                        ),
                        id = 0,
                        sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                    ))
                }
                add(UiMessage.Incoming(
                    message = Message(
                        text = "Message #",
                        sendId = 1213,
                        sender = WsPlayerData.emptyPlayer.copy(name = "user${2}", clientId = (2).toString()),
                        sentAtEpochSecond = Clock.System.now().epochSeconds
                    )
                ))
                add(UiMessage.Outgoing(
                    loading = false,
                    message = Message(
                        text = "Message #Send ",
                        sendId = 1213,
                        sender = WsPlayerData.emptyPlayer.copy(name = "me", clientId = "123"),
                        sentAtEpochSecond = Clock.System.now().epochSeconds
                    ),
                    id = 0,
                    sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                ))
            }
        )
    }
    ThreePaneLayout(
        left = {
            state.postState?.let {
                PostInfo(it, state.postId ?: "")
            } ?: LfgBackgroundBox(Modifier.fillMaxSize()) {
                    LfgText(text = "Loading post info")
            }
        },
        right = {
            Box(
                Modifier
                    .background(DarkBackGround)
                    .fillMaxSize()
            ) {
                Messages(state)
            }
        },
        middle = {
            PostOwner(state = state, events = emptyFlow())
        }
    )
}




