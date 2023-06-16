package io.vallfg.valorantlfgmultiplatform.domain.usecase

/**
 * Wrapper class for Use cases that can use take [E] which is the type for the custom error.
 * And [T] which is the type for the data in a Success case.
 * contains extension functions [UseCaseResponse.onError], [UseCaseResponse.onSuccess] to handle responses
 * in monad style. also has extension [UseCaseResponse.suspendOnError], [UseCaseResponse.suspendOnSuccess]
 * for updating state in the ViewModel.
 */
sealed interface UseCaseResponse <E, out T> {

    sealed class Success<E, T>(open val data: T): UseCaseResponse<E, T>

    sealed class Error<E, T>(open val errors: List<E>): UseCaseResponse<E, T>
}


fun <E, T> UseCaseResponse<E, T>.onSuccess(
    action: (T) -> Unit
) = when(this) {
    is UseCaseResponse.Success -> {
        action(this.data)
        this
    }
    else -> this
}

suspend fun <E, T> UseCaseResponse<E, T>.suspendOnSuccess(
    action: suspend (T) -> Unit
) = when(this) {
    is UseCaseResponse.Success -> {
        action(this.data)
        this
    }
    else -> this
}

fun <E, T> UseCaseResponse<E, T>.onError(
    action: (List<E>) -> Unit
) = when(this) {
    is UseCaseResponse.Error -> {
        action(this.errors)
        this
    }
    else -> this
}

suspend fun <E, T> UseCaseResponse<E, T>.suspendOnError(
    action: suspend (List<E>) -> Unit
) = when(this) {
    is UseCaseResponse.Error -> {
        action(this.errors)
        this
    }
    else -> this
}