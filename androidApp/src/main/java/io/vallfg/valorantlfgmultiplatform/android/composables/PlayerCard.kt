package io.vallfg.valorantlfgmultiplatform.android.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.android.theme.BluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.DarkBluishBlack
import io.vallfg.valorantlfgmultiplatform.android.theme.LightBluishGray
import io.vallfg.valorantlfgmultiplatform.android.theme.LightGray
import io.vallfg.valorantlfgmultiplatform.android.theme.LocalTheme
import io.vallfg.valorantlfgmultiplatform.android.theme.ValFont

@OptIn(ExperimentalLayoutApi::class, ExperimentalTextApi::class)
@Composable
fun PlayerCard(
    modifier: Modifier = Modifier,
    player: PlayerInfo
) {
    val cardColor = DarkBluishBlack
    val headingColor =  LightGray
    val valueColor = Color.White
    val ctx = LocalContext.current
    var trnScoreInfoVisible by remember {
        mutableStateOf(false)
    }

    TrackerScoreInfoPopup(visible = trnScoreInfoVisible) {
        trnScoreInfoVisible = false
    }

    Column (
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(cardColor),
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
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
                value = "VALORANT",
                onValueChange = { _ -> },
                singleLine = true,
                readOnly = true,
                textStyle = TextStyle(
                    fontFamily = ValFont,
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 140.sp,
                    drawStyle = Stroke(
                        miter = 10f,
                        width = 5f,
                        join = StrokeJoin.Round
                    )
                ),
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(ctx)
                        .data(player.iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = player.rank,
                    modifier = Modifier
                        .size(75.dp)
                )
                Column {
                    Text(
                        text = "Rating",
                        color = headingColor,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = player.rank,
                        color = valueColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }
                Column(
                    Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = player.name,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 28.sp
                    )
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray.copy(0.2f))
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "#${player.tag}",
                            color = Color.LightGray,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
        Row(
            Modifier
                .offset(y = (-20).dp)
                .fillMaxWidth()
        ) {
            StatCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                heading = "K/D Ratio",
                value = player.kd.toString(),
                percentile = player.kdPercentile
            )
            StatCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                heading = "Headshot%",
                value = player.headshotPct.toString(),
                percentile = player.headshotPctPercentile
            )
            StatCard(
                modifier = Modifier
                    .weight(1f)
                    .padding(6.dp),
                heading = "Score P/R",
                value = player.scorePerRound.toString(),
                percentile = player.scorePerRoundPercentile
            )
        }
        FlowRow {
            SubStat(
                heading = "Matches",
                value = player.matchesPlayed.toString(),
            )
            SubStat(
                heading = "Kills",
                value = player.kills.toString(),
            )
            SubStat(
                heading = "Win Pct",
                value = player.matchWinPct.toString(),
            )
            SubStat(
                heading = "Most Kills",
                value = player.mostKillsInMatch.toString()
            )
            SubStat(
                heading = "Assists P/M",
                value = player.assistsPerMatch.toString()
            )
            SubStat(
                heading = "Peak Rank",
                value = player.peakRank
            )
            SubStat(
                heading = "Dmg P/R",
                value = player.dmgPerRound.toString()
            )
            SubStat(
                heading = "KAD Ratio",
                value = player.kda.toString()
            )
            SubStat(
                heading = "First Blood P/M",
                value = player.firstBloodsPerMatch.toString()
            )
            SubStat(
                heading = "First Deaths P/R",
                value = player.firstDeathsPerRound.toString()
            )
        }
        TrnScoreCard(
            modifier = Modifier
                .padding(12.dp)
                .align(CenterHorizontally),
            score = player.trnPerformanceScore,
            onInfoClick = {
                trnScoreInfoVisible = !trnScoreInfoVisible
            }
        )
    }
}

@Composable
fun TrnScoreCard(
    modifier: Modifier,
    score: Double,
    onInfoClick: () -> Unit,
) {

    val headingColor = LightGray

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(BluishGray)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TrnScoreIcon(
            modifier = Modifier
                .height(70.dp)
                .padding(end = 22.dp),
            score = score
        )
        Column(
            Modifier
                .weight(1f)
                .height(70.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Tracker Score",
                color = headingColor,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                Modifier.weight(1f),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
               Text(
                   text = score.toString(),
                   color = Color.White,
                   fontWeight = FontWeight.Bold,
                   fontSize = 26.sp
               )
               Spacer(modifier = Modifier.width(2.dp))
               Text(
                   text = "/1000",
                   color = headingColor,
                   fontSize = 16.sp,
                   fontWeight = FontWeight.SemiBold,
                   modifier = Modifier.align(Alignment.Top)
               )
            }
        }
        IconButton(onClick = onInfoClick) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Info",
                tint = Color.Red
            )
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    heading: String,
    percentile: Double? = null,
    value: String,
    headingColor: Color = Color(0xff94A6BC),
    valueColor: Color = Color.White
) {

    val cardColor = LightBluishGray

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(cardColor)
            .drawBehind {
                drawLine(
                    color = Color.Red,
                    start = Offset(
                        x = this.size.width * 0.1f,
                        y = this.size.height * 0.2f
                    ),
                    end = Offset(
                        x = this.size.width * 0.1f,
                        y = this.size.height * 0.8f
                    ),
                    strokeWidth = 4f
                )
            },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(Modifier.padding(start = 22.dp)) {
            Text(
                text = heading,
                color = headingColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            percentile?.let {pct ->
                Text(
                    text = "Top $pct%",
                    color = if (pct < 5) Color(0xffA2965A) else headingColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun SubStat(
    modifier: Modifier = Modifier,
    heading: String,
    value: String,
    headingColor: Color = LightGray,
    valueColor: Color = Color.White
) {
    Row(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(6.dp))
            .drawBehind {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(
                        x = this.size.width * 0.1f,
                        y = this.size.height * 0.2f
                    ),
                    end = Offset(
                        x = this.size.width * 0.1f,
                        y = this.size.height * 0.8f
                    ),
                    strokeWidth = 4f
                )
            },
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Column(Modifier.padding(start = 22.dp)) {
            Text(
                text = heading,
                color = headingColor,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun TrackerScoreInfoPopup(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (!visible) {
        return
    }
    val trnInfo = remember {
        listOf(
            TrnRankInfo(
                "S",
                "Exceptional ability for this metric. Will quickly surpass their peers.",
                Color(0xff3ECBFF)
            ),
            TrnRankInfo(
                "A",
                "Above average, being consistently at this level means climbing.",
                Color(0xff50C47A)
            ),
            TrnRankInfo(
                "B",
                "Average performance, consistent with players at your skill level.",
                Color(0xffE6BC5C)
            ),
            TrnRankInfo("C", "Performance in this metric needs improvement.", Color(0xffA7C6CC)),
            TrnRankInfo("D", "Performance in this metric needs improvement.", Color(0xffBF868F))
        )
    }
    Popup(
        alignment = Center,
        onDismissRequest = onDismiss
    ) {
        Column(
            Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.9f)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xff1B2733))
                .padding(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
               Icon(
                   imageVector = Icons.Outlined.Info,
                   contentDescription = "Info",
                   tint = Color(0xff94A6BC),
                   modifier = Modifier.padding(end = 22.dp)
               )
                Text(
                    text = "Tracker Score",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Your Tracker Score is a personal performance rating out of 1,000 possible points.",
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            for (info in trnInfo) {
                Column(
                    Modifier.fillMaxWidth()
                ) {
                    Row(Modifier
                        .weight(1f, false)
                        .drawBehind {
                            drawLine(
                                color = info.color,
                                start = Offset(
                                    x = 0f,
                                    y = this.size.height * 0.1f
                                ),
                                end = Offset(
                                    x = 0f,
                                    y = this.size.height * 0.9f
                                ),
                                strokeWidth = 8f
                            )
                        }
                        .padding(start = 12.dp)
                    ) {
                        Column {
                            Spacer(modifier = Modifier.width(18.dp))
                            Text(text = info.letter, color = info.color, fontWeight = FontWeight.Bold)
                            Text(text = info.description, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

private data class TrnRankInfo(
    val letter: String,
    val description: String,
    val color: Color
)