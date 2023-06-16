package io.vallfg.valorantlfgmultiplatform.domain.usecase


/**
 * Use case to parse the player name as a single string into a name + tag that can be used.
 * Returns the name and tag after splitting at # and trimming whitespace from the ends.
 * Uses [UseCaseResponse] wrapper for return values.
 * - Success returns [Pair] first value is name, second value is tag.
 * - Error   return list of [ParseError].
 */
class ParsePlayerNameUseCase {

    private val TAG = "ParsePlayerNameUseCase"

    operator fun invoke(account: String): ParseResponse {
        val errors = buildList {
            if (account.none { it == '#' }) {
                add(ParseError.MissingTag)
            }
            if (account.isBlank()) {
                add(ParseError.Empty)
            }
        }
        if (errors.isNotEmpty()) {
            return ParseResponse.Error(errors)
        }

        val name = account.takeWhile { it != '#' }.trim()
        val tag = account.takeLastWhile { it != '#' }.trim()

        return when {
            name.isBlank() -> ParseResponse.Error(listOf(ParseError.MissingTag))
            tag.isBlank() -> ParseResponse.Error(listOf(ParseError.MissingTag))
            else -> ParseResponse.Success(name, tag)
        }

    }
}



sealed interface ParseResponse: UseCaseResponse<ParseError, Pair<String, String>> {
    data class Success(
        val name: String,
        val tag: String
    ): UseCaseResponse.Success<ParseError, Pair<String, String>>(name to tag), ParseResponse

    data class Error(
        val error: List<ParseError>
    ): UseCaseResponse.Error<ParseError, Pair<String, String>>(error), ParseResponse
}

sealed interface ParseError {
    object Empty: ParseError
    object MissingTag: ParseError
}
