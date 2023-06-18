@file:OptIn(ExperimentalCoroutinesApi::class)

package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.serialization.WebsocketConverterNotFoundException
import io.ktor.serialization.WebsocketDeserializeException
import io.vallfg.valorantlfgmultiplatform.GameMode
import io.vallfg.valorantlfgmultiplatform.Rank
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class WebsocketsRepo (
    private val client: HttpClient,
    private val cookie: HttpHeader?,
    private val json: Json,
    private val dispatcher: CoroutineDispatcher = newFixedThreadPoolContext(300, "IO")
) {

    private val tag = "WebsocketsRepo"

    private var conn: DefaultClientWebSocketSession? = null

    private val mutableReceiveFlow = MutableSharedFlow<WsData>()
    val receiveFlow = mutableReceiveFlow.asSharedFlow()

    suspend fun joinPost(
        postId: String
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            client.webSocket(
                request = {
                    url("http://10.0.2.2/join/$postId") // /join/{id}
                    cookie?.let {
                        header(it.name, it.value)
                    }
                    build()
                }
            ) {
                conn = this
                handleWsMessages()
            }
        }
    }

    suspend fun createPost(
        minRank: Rank,
        gameMode: GameMode,
        needed: Int
    ): Result<Unit> = runCatching {
        withContext(dispatcher) {
            client.webSocket(
                request = {
                    url("http://10.0.2.2/join/${needed}/${minRank.string}/${gameMode.string}") // /create/{needed}/{minrank}/{gamemode}
                    cookie?.let {
                        header(it.name, it.value)
                    }
                    build()
                }
            ) {
                conn = this
                try { handleWsMessages() }
                finally {

                }
            }
        }
    }

    suspend fun send(data: WsData): Result<Unit> = runCatching {
        conn?.sendSerialized(data)
    }

    private suspend fun DefaultClientWebSocketSession.handleWsMessages() {
        while(true) {
            try {
                val data = receiveDeserialized<WsData>()
                mutableReceiveFlow.emit(data)
            } catch (e: WebsocketConverterNotFoundException) {
                e.printStackTrace()
            } catch (e: WebsocketDeserializeException) {
                e.printStackTrace()
            }
        }
    }
}