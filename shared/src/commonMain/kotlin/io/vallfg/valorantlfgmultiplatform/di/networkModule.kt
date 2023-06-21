package io.vallfg.valorantlfgmultiplatform.di

import com.apollographql.apollo3.ApolloClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.vallfg.valorantlfgmultiplatform.network.ApiRepo
import io.vallfg.valorantlfgmultiplatform.network.CookieInterceptor
import io.vallfg.valorantlfgmultiplatform.network.LoggingInterceptor
import io.vallfg.valorantlfgmultiplatform.network.WebsocketsRepo
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {

    single<Json> {
        Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single { CookieInterceptor() }
    single { LoggingInterceptor() }

    single {
        ApolloClient.Builder()
            .addHttpInterceptor(get<LoggingInterceptor>())
            .addHttpInterceptor(get<CookieInterceptor>())
            .serverUrl("http://10.0.2.2:8080/graphql")
            .build()
    }

    single {
        ApiRepo(get())
    }

    single {
        WebsocketsRepo(
            client = get(),
            cookieInterceptor = get(),
            json = get(),
            dispatcher = get()
        )
    }

    single<HttpClient> {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    get<Json>()
                )
            }
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(get<Json>())
            }
        }
    }
}