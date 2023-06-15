package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

class WebsocketsRepo(
    private val client: HttpClient,
    private val cookie: HttpHeader?,
    private val json: Json
) {

    suspend fun connect() {

    }
}