package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.screen_models.post_view.PlayerPreviewScreenModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val playerPreviewModule = module {

    factoryOf(::PlayerPreviewScreenModel)
}