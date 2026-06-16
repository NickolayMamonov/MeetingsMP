package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.InMemoryTokenProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class MeetingsClientTest {

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
}

