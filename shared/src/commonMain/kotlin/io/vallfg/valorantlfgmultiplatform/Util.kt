package io.vallfg.valorantlfgmultiplatform

import kotlin.math.roundToInt

fun Double.roundTwoDecPlaces() = (this * 100.0).roundToInt() / 100.0

//fun PlayerMutation.SignInAsPlayer.toPlayerInfo(): PlayerInfo {
//    return PlayerInfo(
//        name = name,
//        tag = tag,
//        kd = kd.roundTwoDecPlaces(),
//        kda = kda.roundTwoDecPlaces(),
//        headshotPct = headshotPct.roundTwoDecPlaces(),
//        matchWinPct = matchWinPct.roundTwoDecPlaces(),
//        matchesPlayed = matchesPlayed,
//        killsPerMatch = killsPerMatch.roundTwoDecPlaces(),
//        mostKillsInMatch = mostKillsInMatch,
//        rank = rank,
//        iconUrl = iconUrl,
//        trnPerformanceScore = trnPerformanceScore.roundTwoDecPlaces(),
//        kills = kills,
//        killsPercentile = killsPercentile.roundTwoDecPlaces(),
//        killsPerRound = killsPerRound.roundTwoDecPlaces(),
//        playlist = playlist,
//        seasonName = seasonName,
//        seasonId = seasonId,
//        scorePerRound = scorePerRound.roundTwoDecPlaces(),
//        scorePerRoundPercentile = scorePerRoundPercentile.roundTwoDecPlaces(),
//        assists = assists,
//        assistsPerMatch = assistsPerMatch.roundTwoDecPlaces(),
//        assistsPerRound = assistsPerRound.roundTwoDecPlaces(),
//        peakRank = peakRank,
//        peakRankActName = peakRankActName,
//        firstDeathsPerRound = firstDeathsPerRound.roundTwoDecPlaces(),
//        kdPercentile = kdPercentile.roundTwoDecPlaces(),
//        dmgPerRound = dmgPerRound.roundTwoDecPlaces(),
//        headshotPctPercentile = headshotPctPercentile.roundTwoDecPlaces(),
//        econRating = econRating.roundTwoDecPlaces(),
//        firstBloodsPerMatch = firstBloodsPerMatch.roundTwoDecPlaces(),
//        timePlayed = timePlayed,
//        peakRankIconUrl = peakRankIconUrl
//    )
//}

data class PlayerInfo(
    val name: String,
    val tag: String,
    val seasonId: String,
    val seasonName: String,
    val playlist: String,
    val rank: String,
    val iconUrl: String,
    val matchesPlayed: Int,
    val matchWinPct: Double,
    val kills: Int,
    val killsPercentile: Double,
    val killsPerRound: Double,
    val killsPerMatch: Double,
    val scorePerRound: Double,
    val scorePerRoundPercentile: Double,
    val assists: Int,
    val assistsPerRound: Double,
    val assistsPerMatch: Double,
    val kd: Double,
    val kdPercentile: Double,
    val kda: Double,
    val dmgPerRound: Double,
    val headshotPct: Double,
    val headshotPctPercentile: Double,
    val econRating: Double,
    val firstBloodsPerMatch: Double,
    val firstDeathsPerRound: Double,
    val mostKillsInMatch: Int,
    val timePlayed: Int,
    val trnPerformanceScore: Double,
    val peakRank: String,
    val peakRankIconUrl: String,
    val peakRankActName: String
) {

  companion object {
      val testPlayer = PlayerInfo(
          name = "Silv",
          tag = "004",
          kd = 1.4,
          kda = 1.4,
          headshotPct = 22.0,
          matchWinPct = 22.33,
          matchesPlayed = 2,
          killsPerMatch = 23.0,
          mostKillsInMatch = 55,
          rank = "Unranked",
          iconUrl = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/0.png",
          trnPerformanceScore = 978.0,
          kills = 2344,
          killsPercentile = 10.00,
          killsPerRound = 2.21,
          playlist = "Ranked",
          seasonName = "Act 2 Season 3",
          seasonId = "398745982379827",
          scorePerRound = 235.34,
          scorePerRoundPercentile = 39.00,
          assists = 123,
          assistsPerMatch = 6.0,
          assistsPerRound = 1.0,
          peakRank = "DiamondIII",
          peakRankActName = "Act 2 Season 2",
          firstDeathsPerRound = 0.0,
          kdPercentile = 99.00,
          dmgPerRound = 99.0,
          headshotPctPercentile = 99.0,
          econRating = 344.0,
          firstBloodsPerMatch = 4.0,
          timePlayed = 342,
          peakRankIconUrl = "https://trackercdn.com/cdn/tracker.gg/valorant/icons/tiersv2/20.png"
      )
  }
}

