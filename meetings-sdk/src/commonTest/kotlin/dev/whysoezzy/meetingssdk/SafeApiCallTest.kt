package dev.whysoezzy.meetingssdk

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class SafeApiCallTest {

    @Test
    fun safeApiCall_returnsSuccessOnSuccessfulCall() = kotlinx.coroutines.test.runTest {
        val result = safeApiCall { "hello" }

        assertTrue(result.isSuccess)
        assertEquals("hello", result.getOrNull())
    }

    @Test
    fun safeApiCall_returnsSuccessWithNullValue() = kotlinx.coroutines.test.runTest {
        val result = safeApiCall<String?> { null }

        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull())
    }

    @Test
    fun safeApiCall_wrapsIOException_asNetworkException() = kotlinx.coroutines.test.runTest {
        val ioException = io.ktor.utils.io.errors.IOException("No connection")

        val result = safeApiCall { throw ioException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Network)
        assertTrue(exception!!.message!!.contains("Network error"))
    }

    @Test
    fun safeApiCall_wrapsSerializationException_asSerializationException() = kotlinx.coroutines.test.runTest {
        val serializationException = kotlinx.serialization.SerializationException("Bad JSON")

        val result = safeApiCall { throw serializationException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Serialization)
        assertTrue(exception!!.message!!.contains("Serialization error"))
    }

    @Test
    fun safeApiCall_wrapsGenericException_asUnknownException() = kotlinx.coroutines.test.runTest {
        val genericException = RuntimeException("Unexpected failure")

        val result = safeApiCall { throw genericException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Unknown)
        assertTrue(exception!!.message!!.contains("Unknown error"))
    }

    @Test
    fun safeApiCall_passesThroughApiException() = kotlinx.coroutines.test.runTest {
        val apiException = ApiException.Http(statusCode = 403)

        val result = safeApiCall { throw apiException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Http)
        assertEquals(403, (exception as ApiException.Http).statusCode)
    }
}
