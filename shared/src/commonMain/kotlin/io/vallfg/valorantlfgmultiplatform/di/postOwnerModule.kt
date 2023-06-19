package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.screen_models.post_owner.PostOwnerScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val postOwnerModule = module {

    includes(networkModule)
    includes(domainModule)

    factoryOf(::PostOwnerScreenModel)
}