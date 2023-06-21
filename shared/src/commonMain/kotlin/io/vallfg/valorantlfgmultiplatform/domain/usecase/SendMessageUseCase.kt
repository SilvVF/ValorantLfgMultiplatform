package io.vallfg.valorantlfgmultiplatform.domain.usecase

import cafe.adriel.voyager.core.concurrent.AtomicInt32
import io.vallfg.valorantlfgmultiplatform.network.Message
import io.vallfg.valorantlfgmultiplatform.network.SendMessage
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import io.vallfg.valorantlfgmultiplatform.network.WsPlayerData
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.UiMessage
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val messageId = AtomicInt32(0)

class SendMessageUseCase {

    suspend operator fun invoke(
        websocketsRepo: WebsocketsRepo,
        text: String,
        users: Map<String, WsPlayerData>,
        clientId: String
    ): SendResponse {
        val id = messageId.getAndIncrement()
        val response = websocketsRepo.send(
            SendMessage(text, id)
        )
        response.onSuccess {
            return SendResponse.Success(
                UiMessage.Outgoing(
                    id = id,
                    sentAt = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
                    loading = true,
                    message = Message(
                        text = text,
                        sender = users[clientId] ?: WsPlayerData.emptyPlayer,
                        sentAtEpochSecond = 0L,
                        sendId = id
                    )
                )
            )
        }.onFailure {
            return SendResponse.Error(
                SendMessageError.GenericError(it.message ?: "Error")
            )
        }
        return SendResponse.Error(
            SendMessageError.GenericError("Error")
        )
    }
}


sealed interface SendResponse: UseCaseResponse<SendMessageError, UiMessage.Outgoing> {
    data class Success(
        val message: UiMessage.Outgoing
    ): UseCaseResponse.Success<SendMessageError, UiMessage.Outgoing>(message), SendResponse

    data class Error(
        val error: SendMessageError
    ): UseCaseResponse.Error<SendMessageError, UiMessage.Outgoing>(listOf(error)), SendResponse
}

sealed class SendMessageError(open val message: String) {
    data class GenericError(override val message: String): SendMessageError(message)
}