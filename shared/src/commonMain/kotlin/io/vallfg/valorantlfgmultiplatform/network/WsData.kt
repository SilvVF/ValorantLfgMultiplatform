package io.vallfg.valorantlfgmultiplatform.network

import io.vallfg.type.Player
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface WsData

@Serializable
@SerialName("Message")
data class Message(
    val text: String,
): WsData


@Serializable
@SerialName("PostCreate")
data class PostCreate(
    val id: String,
): WsData