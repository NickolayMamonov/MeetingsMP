package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.InMemoryTokenProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming", "TooManyFunctions")
class MeetingsClientTest {

    // --- InMemoryTokenProvider tests ---

    @Test
    fun inMemoryTokenProvider_storesAndRetrievesTokens() {
        val provider = InMemoryTokenProvider()

        assertFalse(provider.isLoggedIn.value)

        provider.saveTokens(AuthToken(accessToken = "test-access", refreshToken = "test-refresh"))
        assertTrue(provider.isLoggedIn.value)
        assertEquals("test-access", provider.getAccessToken())
        assertEquals("test-refresh", provider.getRefreshToken())

        provider.clearTokens()
        assertFalse(provider.isLoggedIn.value)
    }

    @Test
    fun inMemoryTokenProvider_clearRemovesTokens() {
        val provider = InMemoryTokenProvider()
        provider.saveTokens(AuthToken(accessToken = "my-access", refreshToken = "my-refresh"))
        assertTrue(provider.isLoggedIn.value)

        provider.clearTokens()
        assertFalse(provider.isLoggedIn.value)
        assertNull(provider.getAccessToken())
        assertNull(provider.getRefreshToken())
    }

    @Test
    fun inMemoryTokenProvider_overwritesPreviousTokens() {
        val provider = InMemoryTokenProvider()
        provider.saveTokens(AuthToken(accessToken = "first-access", refreshToken = "first-refresh"))
        assertEquals("first-access", provider.getAccessToken())
        assertEquals("first-refresh", provider.getRefreshToken())

        provider.saveTokens(AuthToken(accessToken = "second-access", refreshToken = "second-refresh"))
        assertEquals("second-access", provider.getAccessToken())
        assertEquals("second-refresh", provider.getRefreshToken())
    }

    @Test
    fun inMemoryTokenProvider_isLoggedInReflectsState() {
        val provider = InMemoryTokenProvider()
        assertFalse(provider.isLoggedIn.value)

        provider.saveTokens(AuthToken(accessToken = "access", refreshToken = "refresh"))
        assertTrue(provider.isLoggedIn.value)

        provider.clearTokens()
        assertFalse(provider.isLoggedIn.value)
    }

    // --- HTTPS validation tests ---

    @Test
    fun meetingsClient_factory_acceptsHttpsBaseUrl() {
        val client = MeetingsClient(
            baseUrl = "https://api.meetings.mp/",
            tokenProvider = InMemoryTokenProvider(),
            allowHttp = false,
        )
        assertFalse(client.isAuthenticated)
    }

    @Test
    fun meetingsClient_factory_acceptsLocalhostHttpBaseUrl() {
        val client = MeetingsClient(
            baseUrl = "http://localhost:8080/",
            tokenProvider = InMemoryTokenProvider(),
            allowHttp = false,
        )
        assertFalse(client.isAuthenticated)
    }

    @Test
    fun meetingsClient_factory_rejectsNonLocalhostHttpBaseUrl() {
        assertFailsWith<IllegalArgumentException> {
            MeetingsClient(
                baseUrl = "http://api.meetings.mp/",
                tokenProvider = InMemoryTokenProvider(),
                allowHttp = false,
            )
        }
    }

    @Test
    fun meetingsClient_factory_rejectsWithDescriptiveMessage() {
        val exception = assertFailsWith<IllegalArgumentException> {
            MeetingsClient(
                baseUrl = "http://api.meetings.mp/",
                tokenProvider = InMemoryTokenProvider(),
                allowHttp = false,
            )
        }
        assertTrue(exception.message?.contains("HTTPS") == true)
        assertTrue(exception.message?.contains("http://api.meetings.mp/") == true)
    }

    @Test
    fun meetingsClient_factory_allowsHttpWhenFlagSet() {
        val client = MeetingsClient(
            baseUrl = "http://staging.meetings.mp/",
            tokenProvider = InMemoryTokenProvider(),
            allowHttp = true,
        )
        assertFalse(client.isAuthenticated)
    }

    @Test
    fun meetingsClient_factory_defaultBaseUrlIsLocalhost() {
        val client = MeetingsClient()
        assertFalse(client.isAuthenticated)
    }
}
