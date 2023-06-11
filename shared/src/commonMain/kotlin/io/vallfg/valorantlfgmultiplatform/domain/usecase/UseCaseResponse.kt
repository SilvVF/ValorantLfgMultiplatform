package io.vallfg.valorantlfgmultiplatform.domain.usecase

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