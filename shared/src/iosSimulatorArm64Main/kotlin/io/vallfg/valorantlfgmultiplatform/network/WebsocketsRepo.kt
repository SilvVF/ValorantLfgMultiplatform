package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import io.ktor.client.HttpClient

actual class WebsocketsRepo actual constructor(
    private val client: HttpClient,
    private val cookie: HttpHeader
) {


    actual suspend fun send() {

    }

    actual suspend fun join(postId: String) {

    }

    actual suspend fun create() {

    }
}