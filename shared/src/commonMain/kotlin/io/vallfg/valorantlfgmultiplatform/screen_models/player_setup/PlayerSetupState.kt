package io.vallfg.valorantlfgmultiplatform.screen_models.player_setup

import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParseError

data class PlayerSetupState(
    val account: String = "",
    val parsingErrors: List<ParseError> = emptyList(),
    val fetching: Boolean = false,
)