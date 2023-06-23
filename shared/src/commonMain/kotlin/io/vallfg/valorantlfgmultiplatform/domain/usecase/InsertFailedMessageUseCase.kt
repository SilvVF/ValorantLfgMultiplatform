package io.vallfg.valorantlfgmultiplatform.domain.usecase
import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.UiMessage

class InsertFailedMessageUseCase {

    operator fun invoke(
        id: Int,
        messages: List<UiMessage>
    ): List<UiMessage> {
        val idxOfSend = messages.indexOfFirst { msg ->
            when (msg) {
                is UiMessage.Failed -> false
                is UiMessage.Incoming -> false
                is UiMessage.Outgoing -> msg.id == id
            }
        }
        val original = messages
            .getOrNull(idxOfSend) as? UiMessage.Outgoing ?: return messages
        return buildList {
                addAll(messages.subList(0, idxOfSend))
                add(
                    UiMessage.Failed(original.message)
                )
                addAll(messages.subList(idxOfSend + 1, messages.size))
        }
    }
}

