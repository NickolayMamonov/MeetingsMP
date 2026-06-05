package dev.whysoezzy.meetings.common.error

import dev.whysoezzy.meetingssdk.ApiException

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
                in 400..499 -> ErrorType.Unauthorized
                in 500..599 -> ErrorType.Server
                else -> ErrorType.Unknown
            }
        }
        is ApiException.Network -> ErrorType.NoConnection
        is ApiException.Serialization -> ErrorType.Unknown
        is ApiException.Unknown -> ErrorType.Unknown
    }
}
