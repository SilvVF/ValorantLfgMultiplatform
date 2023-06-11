package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParsePlayerNameUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::ParsePlayerNameUseCase)
}