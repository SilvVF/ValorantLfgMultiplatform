package io.vallfg.valorantlfgmultiplatform.network

import io.vallfg.valorantlfgmultiplatform.Rank
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface OutWsData

@Serializable
sealed interface InWsData

@Serializable
@SerialName("BanPlayer")
data class BanPlayer(
    val player: WsPlayerData
): InWsData

@Serializable
@SerialName("SendMessage")
data class SendMessage(
    val text: String,
    val sendId: Int,
): InWsData

@Serializable
@SerialName("ClosePost")
data class ClosePost(
    val reason: String
): InWsData

@Serializable
@SerialName("PostClosed")
data class PostClosed(
    val creator: WsPlayerData,
    var banned: List<WsPlayerData> = emptyList(),
    var users: List<WsPlayerData> = emptyList(),
    var messages: List<Message> = emptyList(),
    val minRank: String,
    val needed: Int,
    val gameMode: String
): OutWsData

@Serializable
@SerialName("Message")
data class Message(
    val text: String,
    val sendId: Int,
    val sender: WsPlayerData,
    val sentAtEpochSecond: Long
): OutWsData

@Serializable
@SerialName("PostJoined")
data class PostJoined(
    val id: String,
    val clientId: String,
): OutWsData


@Serializable
@SerialName("Error")
data class Error(
    val message: String,
): OutWsData

@Serializable
@SerialName("PostState")
data class PostState(
    val creator: WsPlayerData,
    var banned: List<WsPlayerData> = emptyList(),
    var users: List<WsPlayerData> = emptyList(),
    var messages: List<Message> = emptyList(),
    val minRank: String,
    val needed: Int,
    val gameMode: String
): OutWsData

@Serializable
@SerialName("PlayerJoined")
data class PlayerJoined(
    val player: WsPlayerData,
): OutWsData

@Serializable
@SerialName("PlayerLeft")
data class PlayerLeft(
    val player: WsPlayerData,
    val banned: Boolean = false,
): OutWsData

@Serializable
data class WsPlayerData (
    val clientId: String,
    val name: String,
    val tag: String,
    val seasonId:  String,
    val seasonName:            String,
    val playlist:               String,
    val rank:                    String,
    val iconUrl:                 String,
    val matchesPlayed:           Int,
    val matchWinPct:            Double,
    val kills:                  Int,
    val killsPercentile:        Double,
    val killsPerRound:          Double,
    val killsPerMatch:           Double,
    val scorePerRound:          Double,
    val scorePerRoundPercentile:Double,
    val assists:                Int,
    val assistsPerRound:         Double,
    val assistsPerMatch:         Double,
    val kd:                      Double,
    val kdPercentile:           Double,
    val kda:                     Double,
    val dmgPerRound:            Double,
    val headshotPct:            Double,
    val headshotPctPercentile:   Double,
    val econRating:              Double,
    val firstBloodsPerMatch:    Double,
    val firstDeathsPerRound:     Double,
    val mostKillsInMatch:        Int,
    val timePlayed:              Int,
    val trnPerformanceScore:     Double,
    val peakRank:                String,
    val peakRankIconUrl:        String,
    val peakRankActName:         String,
) {

    companion object {
        val emptyPlayer by lazy {
            WsPlayerData(
                "", "name", "tag", "", "", "", Rank.values().random().string, "", 0,
                0.0, 0, 0.0, 0.0, 0.0, 0.0, 0.0, 0, 0.0, 0.0,
                0.00,0.0,0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
                0, 0, 0.0, "0.0", "0.0",
                ""
            )
        }
    }
}


