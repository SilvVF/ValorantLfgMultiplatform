package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PostViewScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val postViewModule = module {

    includes(networkModule)

    factoryOf(::PostViewScreenModel)
}