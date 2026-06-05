package dev.whysoezzy.meetingssdk

import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.InMemoryTokenProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming")
class MeetingsClientTest {

    @Test
    fun inMemoryTokenProvider_storesAndRetrievesToken() {
        val provider = InMemoryTokenProvider()

        assertFalse(provider.isAuthenticated())

        provider.setToken(AuthToken("test-token"))
        assertTrue(provider.isAuthenticated())
        assertEquals("test-token", provider.getToken()!!.token)

        provider.clear()
        assertFalse(provider.isAuthenticated())
    }

    @Test
    fun inMemoryTokenProvider_clearRemovesToken() {
        val provider = InMemoryTokenProvider()
        provider.setToken(AuthToken("my-token"))
        assertTrue(provider.isAuthenticated())

        provider.clear()
        assertFalse(provider.isAuthenticated())
    }

    @Test
    fun inMemoryTokenProvider_setNullRemovesToken() {
        val provider = InMemoryTokenProvider()
        provider.setToken(AuthToken("my-token"))
        assertTrue(provider.isAuthenticated())

        provider.setToken(null)
        assertFalse(provider.isAuthenticated())
    }

    private fun InMemoryTokenProvider.isAuthenticated(): Boolean = getToken() != null
}
