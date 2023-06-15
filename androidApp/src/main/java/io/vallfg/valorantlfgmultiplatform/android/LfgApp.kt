package io.vallfg.valorantlfgmultiplatform.android

import android.app.Application
import cafe.adriel.voyager.core.registry.ScreenRegistry
import io.vallfg.valorantlfgmultiplatform.android.screens.PlayerSetupScreen
import io.vallfg.valorantlfgmultiplatform.android.screens.PlayerViewScreen
import io.vallfg.valorantlfgmultiplatform.android.screens.PostViewScreen
import io.vallfg.valorantlfgmultiplatform.android.screens.PostViewScreenProps
import io.vallfg.valorantlfgmultiplatform.di.appModule
import io.vallfg.valorantlfgmultiplatform.di.domainModule
import io.vallfg.valorantlfgmultiplatform.di.networkModule
import io.vallfg.valorantlfgmultiplatform.di.playerSetupModule
import io.vallfg.valorantlfgmultiplatform.nav.SharedScreen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class LfgApp: Application() {

    override fun onCreate() {
        super.onCreate()
        ScreenRegistry {
            register<SharedScreen.PlayerSetup> {
                PlayerSetupScreen()
            }

            register<SharedScreen.PlayerView> { provider ->
                PlayerViewScreen(provider.playerInfo)
            }
            register<SharedScreen.PostView> {
                PostViewScreen(PostViewScreenProps(it.name, it.tag))
            }
        }
        startKoin {
            androidContext(this@LfgApp.applicationContext)
            androidLogger(Level.DEBUG)
            modules(
                appModule,
                networkModule,
                domainModule,
                playerSetupModule
            )
        }
    }
}