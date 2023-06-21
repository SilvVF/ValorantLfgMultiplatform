package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.domain.usecase.ApplyPostFiltersUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.FilterPostsUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.InsertFailedMessageUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.ParsePlayerNameUseCase
import io.vallfg.valorantlfgmultiplatform.domain.usecase.SendMessageUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {

    factoryOf(::ParsePlayerNameUseCase)

    factoryOf(::ApplyPostFiltersUseCase)

    factoryOf(::FilterPostsUseCase)

    factoryOf(::InsertFailedMessageUseCase)

    factoryOf(::SendMessageUseCase)
}