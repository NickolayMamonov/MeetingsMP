package dev.whysoezzy.meetings.common.error

import dev.whysoezzy.meetingssdk.ApiException

private const val HTTP_CLIENT_ERROR_START = 400
private const val HTTP_CLIENT_ERROR_END = 499
private const val HTTP_SERVER_ERROR_START = 500
private const val HTTP_SERVER_ERROR_END = 599

/**
 * Maps an [ApiException] to a user-friendly [ErrorType].
 *
 * This conversion is used by ViewModels to transform SDK-level
 * exceptions into UI-level error states.
 */
fun ApiException.toErrorType(): ErrorType {
    return when (this) {
        is ApiException.Http -> {
            when (statusCode) {
                in HTTP_CLIENT_ERROR_START..HTTP_CLIENT_ERROR_END -> ErrorType.Unauthorized
                in HTTP_SERVER_ERROR_START..HTTP_SERVER_ERROR_END -> ErrorType.Server
                else -> ErrorType.Unknown
            }
        }
        is ApiException.Network -> ErrorType.NoConnection
        is ApiException.Serialization -> ErrorType.Unknown
        is ApiException.Unknown -> ErrorType.Unknown
    }
}
