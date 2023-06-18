@file:OptIn(ExperimentalFoundationApi::class)

package io.vallfg.valorantlfgmultiplatform.android.composables.post_create

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgScaffold
import io.vallfg.valorantlfgmultiplatform.android.atoms.LfgText
import io.vallfg.valorantlfgmultiplatform.android.atoms.TopAppBarStyle
import io.vallfg.valorantlfgmultiplatform.android.theme.ValFont

data class RankItem(
    val rank: Rank,
    val imageUrl: String = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/${rank.value + 2}.png"
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun PostCreate(
    navigateBack: () -> Unit
) {
    LfgScaffold(
        modifier = Modifier.fillMaxSize(),
        topAppBarStyle = TopAppBarStyle(
            backgroundColor = Color.Transparent,
            navigationIcon = {
                IconButton(
                    onClick = navigateBack
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "back",
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
        ),
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets
            .exclude(WindowInsets.systemBars)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            var selectedGameModeIdx by remember {
                mutableStateOf(0)
            }
            var selectedNeededIdx by remember {
                mutableStateOf(0)
            }

            val gameModes = remember {
                GameMode.values().map { it.string }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xff19245B),
                                Color(0xff44274C),
                                Color(0xff65263E)
                            )
                        )
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.Center),
                    value = "VALORANT",
                    onValueChange = { _ -> },
                    singleLine = true,
                    readOnly = true,
                    textStyle = TextStyle(
                        fontFamily = ValFont,
                        color = Color.White.copy(alpha = 0.4f),
                        fontSize = 200.sp,
                        drawStyle = Stroke(
                            miter = 10f,
                            width = 5f,
                            join = StrokeJoin.Round
                        )
                    ),
                )
            }
            RankPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, false)
            ) { rank ->

            }
            Spacer(modifier = Modifier.height(12.dp))
            LfgText(
                text = "Game Mode",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(12.dp)
            )
            LfgSelector(
                items = gameModes,
                selectedIdx = selectedGameModeIdx,
                onSelectionChanged = { idx ->
                    selectedGameModeIdx = idx
                }
            )
            Spacer(modifier = Modifier.height(64.dp))
            LfgText(
                text = "Players Needed",
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(12.dp)
            )
            LfgSelector(
                items = listOf("1", "2", "3", "4"),
                selectedIdx = selectedNeededIdx,
                onSelectionChanged = { idx ->
                    selectedNeededIdx = idx
                }
            )
        }
    }

}