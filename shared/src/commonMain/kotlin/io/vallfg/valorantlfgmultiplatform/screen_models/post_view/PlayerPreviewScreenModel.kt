package io.vallfg.valorantlfgmultiplatform.screen_models.post_view

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.suspendOnFailure
import io.vallfg.valorantlfgmultiplatform.network.suspendOnSuccess
import io.vallfg.valorantlfgmultiplatform.toPlayerInfo
import kotlinx.coroutines.launch

class PlayerPreviewScreenModel(
    private val apiRepo: ApiRepo
): StateScreenModel<PlayerPreviewState>(PlayerPreviewState.Loading) {

    fun loadPlayers(players: List<List<String>>) = coroutineScope.launch {
        if (true) {
            mutableState.value = PlayerPreviewState.Success(
                players = List((1..4).random()) { PlayerInfo.testPlayer }
            )
            return@launch
        }
        players.filter { it.size == 2 }.forEach { it ->
            apiRepo.getPlayer(name = it.first(),tag = it.last())
                .suspendOnSuccess { result ->
                    result.toPlayerInfo()?.let {
                        mutableState.value = PlayerPreviewState.Success(
                            players = state.value.players + it
                        )
                    }
                }
                .suspendOnFailure {
                    mutableState.value = PlayerPreviewState.Failure(it)
                }
        }
    }
}

sealed class PlayerPreviewState(
    open val players: List<PlayerInfo> = emptyList(),
) {

    object Loading: PlayerPreviewState()

    data class Failure(
        val errors: List<String> = emptyList()
    ): PlayerPreviewState()

    data class Success(
        override val players: List<PlayerInfo>
    ): PlayerPreviewState(players)
}