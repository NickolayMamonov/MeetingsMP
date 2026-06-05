package dev.whysoezzy.meetingssdk

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class SafeApiCallTest {

    @Test
    fun safeApiCall_returnsSuccessOnSuccessfulCall() = runBlocking {
        val result = safeApiCall { "hello" }

        assertTrue(result.isSuccess)
        assertEquals("hello", result.getOrNull())
    }

    @Test
    fun safeApiCall_returnsSuccessWithNullValue() = runBlocking {
        val result = safeApiCall<String?> { null }

        assertTrue(result.isSuccess)
        assertEquals(null, result.getOrNull())
    }

    @Test
    fun safeApiCall_wrapsIOException_asNetworkException() = runBlocking {
        val ioException = io.ktor.utils.io.errors.IOException("No connection")

        val result = safeApiCall { throw ioException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Network)
        assertTrue(exception!!.message!!.contains("Network error"))
    }

    @Test
    fun safeApiCall_wrapsSerializationException_asSerializationException() = runBlocking {
        val serializationException = kotlinx.serialization.SerializationException("Bad JSON")

        val result = safeApiCall { throw serializationException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Serialization)
        assertTrue(exception!!.message!!.contains("Serialization error"))
    }

    @Test
    fun safeApiCall_wrapsGenericException_asUnknownException() = runBlocking {
        val genericException = RuntimeException("Unexpected failure")

        val result = safeApiCall { throw genericException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Unknown)
        assertTrue(exception!!.message!!.contains("Unknown error"))
    }

    @Test
    fun safeApiCall_passesThroughApiException() = runBlocking {
        val apiException = ApiException.Http(statusCode = 403)

        val result = safeApiCall { throw apiException }

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is ApiException.Http)
        assertEquals(403, (exception as ApiException.Http).statusCode)
    }
}
