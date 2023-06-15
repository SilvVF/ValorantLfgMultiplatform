package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.screen_models.player_setup.PlayerSetupScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val playerSetupModule = module {

    includes(domainModule)
    includes(networkModule)

    factoryOf(::PlayerSetupScreenModel)
}