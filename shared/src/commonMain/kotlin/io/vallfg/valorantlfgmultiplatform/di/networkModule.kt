package io.vallfg.valorantlfgmultiplatform.di

import com.apollographql.apollo3.ApolloClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.cookie
import io.ktor.client.request.header
import io.ktor.http.Cookie
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.CookieInterceptor
import io.vallfg.valorantlfgmultiplatform.network.LoggingInterceptor
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
        ApolloClient.Builder()
            .addHttpInterceptor(LoggingInterceptor())
            .addHttpInterceptor(CookieInterceptor())
            .serverUrl("http://10.0.2.2:8080/graphql")
            .build()
    }

    single {
        ApiRepo(get())
    }

    single {
        HttpClient(CIO) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(get())
            }
        }
    }
}