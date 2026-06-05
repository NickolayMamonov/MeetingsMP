package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.models.ErrorResponse

/**
 * Sealed hierarchy of exceptions that can occur during API calls.
 *
 * Each subclass represents a distinct failure category, making it easy to
 * handle errors in a type-safe way at the call site.
 */
sealed class ApiException : Exception {

    /** HTTP-level error with status code and optional parsed body. */
    data class Http(
        val statusCode: Int,
        val errorResponse: ErrorResponse? = null,
    ) : ApiException() {
        override val message: String
            get() = "HTTP $statusCode: ${errorResponse?.message ?: "Unknown error"}"
    }

    /** Network connectivity failure (no route to host, DNS failure, etc.). */
    data class Network(
        override val cause: Throwable? = null,
    ) : ApiException() {
        override val message: String = "Network error: ${cause?.message ?: "No connection"}"
    }

    /** Request or response serialization failure. */
    data class Serialization(
        override val cause: Throwable? = null,
    ) : ApiException() {
        override val message: String = "Serialization error: ${cause?.message ?: "Unknown"}"
    }

    /** An unexpected error that does not fit the other categories. */
    data class Unknown(
        override val cause: Throwable? = null,
    ) : ApiException() {
        override val message: String = "Unknown error: ${cause?.message ?: "No details"}"
    }

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(cause: Throwable) : super(cause)
}
