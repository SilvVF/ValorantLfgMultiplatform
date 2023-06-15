@file:OptIn(ExperimentalFoundationApi::class, ExperimentalComposeApi::class)

package io.vallfg.valorantlfgmultiplatform.android.screens


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.vallfg.PostQuery
import io.vallfg.type.Player
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.Post
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewScreenModel
import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewState

data class PostViewScreenProps(
    val name: String,
    val tag: String
)

class PostViewScreen(
    private val props: PostViewScreenProps
): Screen {

    @Composable
    override fun Content() {

        val screenModel = getScreenModel<PostViewScreenModel>()

        val state = screenModel.state.collectAsState().value
        
        when (state) {
            is PostViewState.Success -> {
                PostView(state = state)
            }
            else -> {
                Surface(Modifier.fillMaxSize()) {

                }
            }
        }
    }
}

@Composable
fun PostView(
    state: PostViewState.Success
) {

    LfgScaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)

        ) {
            items(
                items = state.posts,
                key = { post -> post.id },
            ) {
                PostCard(
                    post = it,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .animateItemPlacement()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

}

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    post: Post,
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
            Row(verticalAlignment = CenterVertically) {
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
            Row {
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
    }
}

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