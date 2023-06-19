package io.vallfg.valorantlfgmultiplatform.di

import io.vallfg.valorantlfgmultiplatform.LfgDispatcher
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
        playerPreviewModule,
        postOwnerModule
    )

    single<DestinationsNavigator> {
        LfgAppComposeNavigator()
    }

    single<LfgDispatcher> {
        LfgDispatcher()
    }
}

