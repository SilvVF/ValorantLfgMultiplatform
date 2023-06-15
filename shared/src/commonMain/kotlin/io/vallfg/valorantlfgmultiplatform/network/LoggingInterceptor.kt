package io.vallfg.valorantlfgmultiplatform.network

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import io.vallfg.valorantlfgmultiplatform.logging.Log

class LoggingInterceptor: HttpInterceptor {

    private val tag = "LoggingInterceptor"

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {


        for (header in request.headers) {
            Log.d(tag, header.toString())
        }

        return chain.proceed(request).also {
            for (header in it.headers) {
                Log.d(tag, header.toString())
            }
        }
    }
}