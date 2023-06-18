package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.nav.DestinationsNavigator
import io.vallfg.valorantlfgmultiplatform.nav.LfgAppComposeNavigator
import org.koin.dsl.module

val appModule = module {

    includes(
        playerSetupModule,
        domainModule,
        postViewModule,
        networkModule,
        domainModule,
        playerPreviewModule
    )

    single<DestinationsNavigator> {
        LfgAppComposeNavigator()
    }
}

