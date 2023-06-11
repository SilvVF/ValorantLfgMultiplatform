package io.vallfg.valorantlfgmultiplatform.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single {
        HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
            }
        }
    }
}