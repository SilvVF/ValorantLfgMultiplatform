package io.vallfg.valorantlfgmultiplatform.android.composables.post_view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant

@Composable
fun PostListItem(
    modifier: Modifier = Modifier,
    post: Post,
    onPlayerIconsClick: (players: List<List<String>>) -> Unit
) {
    val ctx = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ImageRequest.Builder(ctx)
                .data(post.minRankIconUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                LfgText(
                    text = "Min Rank |",
                    color = LightGray,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 2.dp)
                )
                LfgText(
                    text = post.minRank.string,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(Modifier.clickable { onPlayerIconsClick(post.players) }) {
                for (i in 0..post.needed) {
                    PlayerIcon(
                        playerName = post.players.getOrNull(i)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            LfgText(
                text = post.gameMode.string,
                fontWeight = FontWeight.SemiBold
            )
        }

        val timeSincePost by remember {
            derivedStateOf {
                val now = Clock.System.now().epochSeconds
                val postTimeEpochSeconds = post.date.toInstant(TimeZone.currentSystemDefault()).epochSeconds
                val minutesSince = (now - postTimeEpochSeconds) / 60
                if (minutesSince < 1) {
                    "<1"
                } else {
                    "${minutesSince.toInt()}m"
                }
            }
        }
        Box(
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.TopEnd
        ) {
            LfgText(
                text = timeSincePost,
                color = LightGray,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}