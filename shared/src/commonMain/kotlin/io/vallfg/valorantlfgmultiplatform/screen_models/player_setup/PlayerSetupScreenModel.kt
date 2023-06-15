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
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.suspendOnError
import io.vallfg.valorantlfgmultiplatform.network.suspendOnException
import io.vallfg.valorantlfgmultiplatform.network.suspendOnSuccess
import io.vallfg.valorantlfgmultiplatform.toPlayerInfo
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
    private val apiRepo: ApiRepo,
    private val parsePlayerNameUseCase: ParsePlayerNameUseCase
): StateScreenModel<PlayerSetupState>(PlayerSetupState()) {

    private val TAG = "PlayerSetupScreenModel"
    private val _errorChannel = Channel<String>()
    val errorChannel = _errorChannel.receiveAsFlow()

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
                signPlayerIn(name, tag)
            }
            .onError { err ->
                mutableState.getAndUpdate {
                    it.copy(
                        parsingErrors = err
                    )
                }
            }
    }

    private fun signPlayerIn(name: String, tag: String) = coroutineScope.launch {
        mutableState.getAndUpdate { it.copy(fetching = true) }
        apiRepo.login(name, tag)
            .suspendOnSuccess {
                val player = it.loginAsPlayer
                navigator.push(
                    ScreenRegistry.get(
                        SharedScreen.PlayerView(player.toPlayerInfo())
                    )
                )
            }
            .suspendOnError { errors ->
                errors.forEach {
                    _errorChannel.send(it)
                }
            }
            .suspendOnException { err ->
                _errorChannel.send(err)
            }
        mutableState.getAndUpdate { it.copy(fetching = false) }
    }
}


