package io.vallfg.valorantlfgmultiplatform.android.composables.post_owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.roundTwoDecPlaces
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenState

data class JoinedPlayerItem(
    val title: String,
    val valuesToImageUrl: List<Pair<String, String?>>,
)


@Composable
fun rememberJoinedPlayerItems(
    state: PostOwnerScreenState.Success
) = remember(state.users) {
    derivedStateOf {
        listOf(
            JoinedPlayerItem(
                "Player",
                valuesToImageUrl = state.users.map { it.value.name to null }
            ),
            JoinedPlayerItem(
                "Rating",
                valuesToImageUrl = state.users.map {
                    it.value.rank to "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/${Rank.fromString(it.value.rank).value + 2}.png"
                }
            ),
            JoinedPlayerItem(
                "Win %",
                valuesToImageUrl = state.users.map {
                    it.value.matchWinPct.roundTwoDecPlaces().toString() to null
                }
            ),
            JoinedPlayerItem(
                "K/D",
                valuesToImageUrl = state.users.map {
                    it.value.kd.roundTwoDecPlaces().toString() to null
                }
            ),
            JoinedPlayerItem(
                "Dmg/Rnd",
                valuesToImageUrl = state.users.map {
                    it.value.dmgPerRound.roundTwoDecPlaces().toString() to null
                }
            )
        )
    }
}

@Composable
fun JoinedPlayersStats(
    modifier: Modifier,
    headingToValues: List<JoinedPlayerItem>,
    onPlayerInfoIconClick: (idx: Int) -> Unit
) {
    val ctx = LocalContext.current
    LazyRow(
        modifier = modifier
    ) {
        itemsIndexed(headingToValues){ idx, item ->
            PlayerInfoLayout(
                modifier = Modifier
            ) {
                Box(
                    Modifier
                        .background(BluishGray)
                        .padding(4.dp)
                ) {
                    LfgText(
                        text = item.title,
                        fontSize = 20.sp,
                        color = LightGray
                    )
                }
                item.valuesToImageUrl.forEachIndexed { i, (value, url) ->
                    Row(
                        modifier = Modifier
                            .background(
                                DarkBluishBlack.copy(
                                    alpha = 1f - i * 0.1f
                                )
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (idx == 0) {
                            IconButton(
                                onClick = {
                                    onPlayerInfoIconClick(i)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        if (url != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(ctx)
                                    .data(url)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(42.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                        LfgText(
                            text = value,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerInfoLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        // Don't constrain child views further, measure them with given constraints
        // List of measured children

        val maxWidth = measurables.maxOf { it.maxIntrinsicWidth(0) }

        val placeables = measurables.mapIndexed { i, measurable ->
            // Measure each children
            measurable.measure(
                Constraints.fixed(
                    maxWidth,
                    if (i == 0) measurable.minIntrinsicHeight(maxWidth) else 200
                )
            )
        }


        // Set the size of the layout as big as it can
        layout(maxWidth, constraints.maxHeight) {
            // Track the y co-ord we have placed children up to
            var yPosition = 0

            // Place children in the parent layout
            placeables.forEach { placeable ->

                // Position item on the screen
                placeable.placeRelative(x = 0, y = yPosition)

                // Record the y co-ord placed up to
                yPosition += placeable.height
            }
        }
    }
}
