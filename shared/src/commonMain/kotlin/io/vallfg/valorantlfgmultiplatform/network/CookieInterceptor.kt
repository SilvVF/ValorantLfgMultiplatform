package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpHeader
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import io.ktor.http.CookieEncoding
import io.ktor.http.decodeCookieValue
import io.vallfg.valorantlfgmultiplatform.logging.Log

/**
 * Intercepts Set-Cookie from graphql responses.
 * @property setCookieHeader contains the most recent intercepted cookie.
 */
class CookieInterceptor: HttpInterceptor {

    var setCookieHeader: HttpHeader? = null
        private set

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {

        val cookieName = "Set-Cookie"

        return  chain.proceed(request).also { response ->
            response.headers
                .find { it.name == cookieName }
                ?.let { header ->
                    setCookieHeader = header
                }
        }
    }
}