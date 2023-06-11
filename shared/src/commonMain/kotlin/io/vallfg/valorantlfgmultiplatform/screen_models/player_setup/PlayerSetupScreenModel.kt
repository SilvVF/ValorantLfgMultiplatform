package io.vallfg.valorantlfgmultiplatform.screen_models.player_setup

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import cafe.adriel.voyager.core.registry.ScreenRegistry
import io.vallfg.valorantlfgmultiplatform.PlayerInfo
import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParseError
import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParsePlayerNameUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.onError
import io.vallfg.valorantlfgmultiplatform.domain.usecase.onSuccess
import io.vallfg.valorantlfgmultiplatform.domain.usecase.suspendOnSuccess
import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.nav.SharedScreen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayerSetupScreenModel(
    private val navigator: DestinationsNavigator,
    private val parsePlayerNameUseCase: ParsePlayerNameUseCase
): StateScreenModel<PlayerSetupState>(PlayerSetupState()) {

    private val TAG = "PlayerSetupScreenModel"
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

    init {
        coroutineScope.launch {
            delay(1000)
            navigator.push(
                ScreenRegistry.get(
                    SharedScreen.PlayerView(PlayerInfo.testPlayer)
                )
            )
        }
    }

    fun handleAccountChanged(acc: String) {
        mutableState.update { it.copy(account = acc) }
        coroutineScope.launch {
            parsePlayerNameUseCase(acc)
                .onSuccess {
                    mutableState.getAndUpdate {
                        it.copy(parsingErrors = emptyList())
                    }
                }
                .onError { err ->
                    mutableState.getAndUpdate {
                        it.copy(parsingErrors = err)
                    }
                }
        }
    }

    fun handleSubmitAccount(account: String) = coroutineScope.launch {
        parsePlayerNameUseCase(account)
            .suspendOnSuccess { (name, tag) ->
                upsertPlayerToApi(name, tag)
            }
            .onError { err ->
                mutableState.getAndUpdate {
                    it.copy(
                        parsingErrors = err
                    )
                }
            }
    }

    private fun upsertPlayerToApi(name: String, tag: String) = coroutineScope.launch {
        mutableState.getAndUpdate { it.copy(fetching = true) }
//        apiRepo.signInAsPlayer(name, tag)
//            .suspendOnSuccess { val player = it.signInAsPlayer
//
//            }
//            .suspendOnError { errors ->
//                errors.forEach {
//                    _errorChannel.send(it)
//                }
//            }
//            .suspendOnException { err ->
//                _errorChannel.send(err)
//            }
        mutableState.getAndUpdate { it.copy(fetching = false) }
    }
}


