package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.models.ErrorResponse
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class ApiExceptionTest {

    @Test
    fun httpApiException_containsStatusCodeAndMessage() {
        val errorResponse = ErrorResponse(
            error = "NOT_FOUND",
            message = "Resource not found",
        )
        val exception = ApiException.Http(
            statusCode = 404,
            errorResponse = errorResponse,
        )

        assertEquals(404, exception.statusCode)
        assertNotNull(exception.errorResponse)
        assertEquals("NOT_FOUND", exception.errorResponse!!.error)
        assertTrue(exception.message!!.contains("404"))
        assertTrue(exception.message!!.contains("Resource not found"))
    }

    @Test
    fun httpApiException_withoutErrorResponse_hasNullBody() {
        val exception = ApiException.Http(statusCode = 500)

        assertEquals(500, exception.statusCode)
        assertNull(exception.errorResponse)
        assertTrue(exception.message!!.contains("500"))
    }

    @Test
    fun networkApiException_preservesCause() {
        val cause = RuntimeException("Connection refused")
        val exception = ApiException.Network(cause = cause)

        assertEquals(cause, exception.cause)
        assertTrue(exception.message!!.contains("Network error"))
    }

    @Test
    fun networkApiException_withoutCause_hasDefaultMessage() {
        val exception = ApiException.Network()

        assertTrue(exception.message!!.contains("No connection"))
    }

    @Test
    fun serializationApiException_preservesCause() {
        val cause = RuntimeException("Invalid JSON")
        val exception = ApiException.Serialization(cause = cause)

        assertEquals(cause, exception.cause)
        assertTrue(exception.message!!.contains("Serialization error"))
    }

    @Test
    fun unknownApiException_preservesCause() {
        val cause = RuntimeException("Something went wrong")
        val exception = ApiException.Unknown(cause = cause)

        assertEquals(cause, exception.cause)
        assertTrue(exception.message!!.contains("Unknown error"))
    }

    @Test
    fun apiExceptionHierarchy_isSealed() {
        val exceptions: List<ApiException> = listOf(
            ApiException.Http(400),
            ApiException.Network(),
            ApiException.Serialization(),
            ApiException.Unknown(),
        )

        assertEquals(4, exceptions.size)
        exceptions.forEach { exception ->
            assertTrue(exception is ApiException)
        }
    }

    @Test
    fun httpApiException_messageFormat_isConsistent() {
        val exception = ApiException.Http(
            statusCode = 401,
            errorResponse = ErrorResponse(error = "UNAUTHORIZED", message = "Invalid token"),
        )

        assertEquals("HTTP 401: Invalid token", exception.message)
    }
}
