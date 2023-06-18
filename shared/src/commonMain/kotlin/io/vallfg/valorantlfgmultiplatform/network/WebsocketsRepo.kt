package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import io.ktor.client.HttpClient


expect class WebsocketsRepo(
    client: HttpClient,
    cookie: HttpHeader
) {

    suspend fun send()

    suspend fun join(postId: String)

    suspend fun create()
}

