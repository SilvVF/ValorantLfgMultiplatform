package  io.vallfg.valorantlfgmultiplatform.network
import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.Operation

/**
 * Wrapper class for Api calls.
 * [Success] contains data if a call was successful.
 * [Error] contains error messages from a bad request or server error.
 * [Exception] contains the error message if an exception was thrown when trying to make the request.
 */
sealed class ApiResponse <out T>  {

    data class Success<out T>(val data: T): ApiResponse<T>()

    data class Error(val messages: List<String>): ApiResponse<Nothing>()

    data class Exception(val message: String): ApiResponse<Nothing>()
}

suspend fun <T> ApiResponse<T>.suspendOnSuccess(
    executable: suspend (T) -> Unit
)  = when(this) {
    is ApiResponse.Error -> this
    is ApiResponse.Exception -> this
    is ApiResponse.Success -> {
        executable(this.data)
        this
    }
}

/**
 * Called when the response is either [ApiResponse.Exception] or [ApiResponse.Error].
 */
suspend fun <T> ApiResponse<T>.suspendOnFailure(
    executable: suspend (errors: List<String>) -> Unit
)  = when(this) {
    is ApiResponse.Error -> {
        executable(this.messages)
        this
    }
    is ApiResponse.Exception -> {
        executable(listOf(this.message))
        this
    }
    is ApiResponse.Success -> this

}

suspend fun ApiResponse<Any>.suspendOnError(
    executable: suspend (errors: List<String>) -> Unit
)  = when(this) {
    is ApiResponse.Error -> {
        executable(this.messages)
        this
    }
    is ApiResponse.Exception -> this
    is ApiResponse.Success -> this
}
suspend fun ApiResponse<Any>.suspendOnException(
    executable: suspend (String) -> Unit
)  = when(this) {
    is ApiResponse.Error -> this
    is ApiResponse.Exception -> {
        executable(this.message)
        this
    }
    is ApiResponse.Success -> this
}

/**
 *  executes the graphql network call and maps the response into an [ApiResponse]
 */
suspend fun <D : Operation.Data, > ApolloCall<D>.executeAsApiResponse(): ApiResponse<D> {
    return try {
        val response = this.execute()
        response.data?.let {
            return ApiResponse.Success(it)
        }
        return ApiResponse.Error(response.errors?.map { it.message } ?: emptyList())
    } catch (e: Exception) {
        e.printStackTrace()
        ApiResponse.Exception(e.message ?: "Unknown Error")
    }
}