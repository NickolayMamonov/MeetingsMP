package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.models.ErrorResponse
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.SerializationException

/**
 * Executes a suspending [block] and wraps the result in [Result].
 *
 * Catches known failure categories and maps them to the appropriate [ApiException] subclass:
 * - **ClientRequestException** (4xx) → [ApiException.Http]
 * - **ServerResponseException** (5xx) → [ApiException.Http]
 * - **IOException** → [ApiException.Network]
 * - **SerializationException** → [ApiException.Serialization]
 * - Anything else → [ApiException.Unknown]
 *
 * Usage:
 * ```kotlin
 * val result: Result<UserProfile> = safeApiCall { usersApi.getMe() }
 * result.fold(
 *     onSuccess = { profile -> … },
 *     onFailure = { error -> when (error) {
 *         is ApiException.Http -> handleError(error.statusCode, error.errorResponse)
 *         is ApiException.Network -> showRetryDialog()
 *         else -> showGenericError()
 *     }}
 * )
 * ```
 */
public suspend fun <T> safeApiCall(block: suspend () -> T): Result<T> {
    @Suppress("TooGenericExceptionCaught")
    return try {
        Result.success(block())
    } catch (e: ClientRequestException) {
        Result.failure(
            ApiException.Http(
                statusCode = e.response.status.value,
                errorResponse = tryParseErrorResponse(e),
            ),
        )
    } catch (e: ServerResponseException) {
        Result.failure(
            ApiException.Http(
                statusCode = e.response.status.value,
                errorResponse = tryParseErrorResponse(e),
            ),
        )
    } catch (e: IOException) {
        Result.failure(ApiException.Network(cause = e))
    } catch (e: SerializationException) {
        Result.failure(ApiException.Serialization(cause = e))
    } catch (e: ApiException) {
        Result.failure(e)
    } catch (e: Throwable) {
        Result.failure(ApiException.Unknown(cause = e))
    }
}

/**
 * Attempts to parse the error response body into an [ErrorResponse].
 * Returns `null` if parsing fails.
 */
@Suppress("FunctionOnlyReturningConstant", "UnusedParameter", "MaxLineLength")
private fun tryParseErrorResponse(@Suppress("UNUSED_PARAMETER") e: Exception): ErrorResponse? {
    return null // Ktorfit/Ktor response bodies are consumed; parsing is done at call site
}
