@file:OptIn(ExperimentalCoroutinesApi::class)

package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.cookie
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.http.Cookie
import io.ktor.http.HttpMethod
import io.ktor.http.append
import io.ktor.http.cookies
import io.ktor.http.parameters
import io.ktor.http.parseServerSetCookieHeader
import io.ktor.serialization.WebsocketConverterNotFoundException
import io.ktor.serialization.WebsocketDeserializeException
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.LfgDispatcher
import io.vallfg.valorantlfgmultiplatform.Rank
import io.vallfg.valorantlfgmultiplatform.logging.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.ThreadLocal


class WebsocketsRepo (
    private val client: HttpClient,
    cookieInterceptor: CookieInterceptor,
    private val json: Json,
    private val dispatcher: LfgDispatcher
) {

    private val tag = "WebsocketsRepo"

    private var conn: DefaultClientWebSocketSession? = null
    private val cookie = cookieInterceptor.setCookieHeader

    suspend fun createPost(
        minRank: Rank,
        gameMode: GameMode,
        needed: Int,
        onDisconnect: suspend () -> Unit,
        onError: suspend (Throwable) -> Unit,
        onReceived: suspend (OutWsData) -> Unit
    ) = runCatching {
        withContext(dispatcher.io) {
            client.webSocket(
                method = HttpMethod.Get,
                host = "10.0.2.2",
                port = 8080,
                path = "/create/$needed/${minRank.string}/${gameMode.string}",
                request = {
                    addCookie(cookie)
                }
            ) {
                conn = this
                try {
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                deserialize(
                                    frame = frame,
                                    onSuccess = { wsData ->
                                        onReceived(wsData)
                                    },
                                    onException = { e ->
                                        e.printStackTrace()
                                    }
                                )
                            }
                            else -> Unit
                        }
                    }
                } finally {
                    onDisconnect()
                }
            }
        }
    }

    private suspend fun deserialize(
        frame: Frame.Text,
        onSuccess: suspend (OutWsData) -> Unit,
        onException: suspend (Throwable) -> Unit
    ) {
        try {
            val data = json.decodeFromString<OutWsData>(
                frame.readText().also { Log.d(tag, it) }
            )
            onSuccess(data)
        } catch (e: WebsocketConverterNotFoundException) {
            onException(e)
        } catch (e: WebsocketDeserializeException) {
            onException(e)
        }
    }

    suspend fun send(data: InWsData): Result<Unit> = runCatching {
        conn?.sendSerialized(data)
    }

    private fun HttpRequestBuilder.addCookie(header: HttpHeader?) {
        header?.let {
            val c = parseServerSetCookieHeader(it.value)
            this.cookie(
                c.name,
                c.value,
                c.maxAge,
                c.expires,
                c.domain,
                c.path,
                c.secure,
                c.httpOnly,
                c.extensions,
            )
        }
    }
}


