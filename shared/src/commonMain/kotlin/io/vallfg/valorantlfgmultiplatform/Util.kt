package io.vallfg.valorantlfgmultiplatform

import io.vallfg.PlayerMutation
import io.vallfg.PlayerQuery
import kotlin.math.roundToInt

fun Double.roundTwoDecPlaces() = (this * 100.0).roundToInt() / 100.0

fun PlayerQuery.Data.toPlayerInfo(): PlayerInfo? {
    val data = this.player?.data ?: return null
    return PlayerInfo(
        name = this.player.name,
        tag = this.player.tag,
        kd = data.kd.roundTwoDecPlaces(),
        kda = data.kda.roundTwoDecPlaces(),
        headshotPct = data.headshotPct.roundTwoDecPlaces(),
        matchWinPct = data.matchWinPct.roundTwoDecPlaces(),
        matchesPlayed = data.matchesPlayed,
        killsPerMatch = data.killsPerMatch.roundTwoDecPlaces(),
        mostKillsInMatch = data.mostKillsInMatch,
        rank = data.rank,
        iconUrl = data.iconUrl,
        trnPerformanceScore = data.trnPerformanceScore.roundTwoDecPlaces(),
        kills = data.kills,
        killsPercentile = data.killsPercentile.roundTwoDecPlaces(),
        killsPerRound = data.killsPerRound.roundTwoDecPlaces(),
        playlist = data.playlist,
        seasonName = data.seasonName,
        seasonId = data.seasonId,
        scorePerRound = data.scorePerRound.roundTwoDecPlaces(),
        scorePerRoundPercentile = data.scorePerRoundPercentile.roundTwoDecPlaces(),
        assists = data.assists,
        assistsPerMatch = data.assistsPerMatch.roundTwoDecPlaces(),
        assistsPerRound = data.assistsPerRound.roundTwoDecPlaces(),
        peakRank = data.peakRank,
        peakRankActName = data.peakRankActName,
        firstDeathsPerRound = data.firstDeathsPerRound.roundTwoDecPlaces(),
        kdPercentile = data.kdPercentile.roundTwoDecPlaces(),
        dmgPerRound = data.dmgPerRound.roundTwoDecPlaces(),
        headshotPctPercentile = data.headshotPctPercentile.roundTwoDecPlaces(),
        econRating = data.econRating.roundTwoDecPlaces(),
        firstBloodsPerMatch = data.firstBloodsPerMatch.roundTwoDecPlaces(),
        timePlayed = data.timePlayed,
        peakRankIconUrl = data.peakRankIconUrl
    )
}

fun PlayerMutation.LoginAsPlayer.toPlayerInfo(): PlayerInfo {
    return PlayerInfo(
        name = name,
        tag = tag,
        kd = data.kd.roundTwoDecPlaces(),
        kda = data.kda.roundTwoDecPlaces(),
        headshotPct = data.headshotPct.roundTwoDecPlaces(),
        matchWinPct = data.matchWinPct.roundTwoDecPlaces(),
        matchesPlayed = data.matchesPlayed,
        killsPerMatch = data.killsPerMatch.roundTwoDecPlaces(),
        mostKillsInMatch = data.mostKillsInMatch,
        rank = data.rank,
        iconUrl = data.iconUrl,
        trnPerformanceScore = data.trnPerformanceScore.roundTwoDecPlaces(),
        kills = data.kills,
        killsPercentile = data.killsPercentile.roundTwoDecPlaces(),
        killsPerRound = data.killsPerRound.roundTwoDecPlaces(),
        playlist = data.playlist,
        seasonName = data.seasonName,
        seasonId = data.seasonId,
        scorePerRound = data.scorePerRound.roundTwoDecPlaces(),
        scorePerRoundPercentile = data.scorePerRoundPercentile.roundTwoDecPlaces(),
        assists = data.assists,
        assistsPerMatch = data.assistsPerMatch.roundTwoDecPlaces(),
        assistsPerRound = data.assistsPerRound.roundTwoDecPlaces(),
        peakRank = data.peakRank,
        peakRankActName = data.peakRankActName,
        firstDeathsPerRound = data.firstDeathsPerRound.roundTwoDecPlaces(),
        kdPercentile = data.kdPercentile.roundTwoDecPlaces(),
        dmgPerRound = data.dmgPerRound.roundTwoDecPlaces(),
        headshotPctPercentile = data.headshotPctPercentile.roundTwoDecPlaces(),
        econRating = data.econRating.roundTwoDecPlaces(),
        firstBloodsPerMatch = data.firstBloodsPerMatch.roundTwoDecPlaces(),
        timePlayed = data.timePlayed,
        peakRankIconUrl = data.peakRankIconUrl
    )
}

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

fun Filterable.getColorHex(): String {
    return when(this) {
        is Needed -> "e5e5e5"
        GameMode.Competitive -> "dc2626"
        GameMode.SpikeRush -> "3b82f6"
        GameMode.Unrated -> "16a34a"
        Rank.Ascendant1, Rank.Ascendant2, Rank.Ascendant3 -> "16a34a"
        Rank.Bronze1,Rank.Bronze2, Rank.Bronze3 -> "854d0e"
        Rank.Diamond1, Rank.Diamond2, Rank.Diamond3 -> "e879f9"
        Rank.Gold1, Rank.Gold2, Rank.Gold3 -> "fde68a"
        Rank.Immortal1, Rank.Immortal2, Rank.Immortal3 -> "dc2626"
        Rank.Iron1, Rank.Iron2, Rank.Iron3 -> "a1a1aa"
        Rank.Plat1, Rank.Plat2, Rank.Plat3 -> "0284c7"
        Rank.Radiant -> "fef9c3"
        Rank.Silver1, Rank.Silver2, Rank.Silver3 -> "e5e5e5"
        Rank.Unranked -> "fafafa"
        else -> "e5e5e5"
    }
}


