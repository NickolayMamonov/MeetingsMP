package dev.whysoezzy.meetings.auth

import com.russhwolf.settings.MapSettings
import dev.whysoezzy.meetings.common.crypto.CryptoHelper
import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.SettingsTokenManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Suppress("FunctionNaming", "TooManyFunctions")
class SettingsTokenManagerTest {

    companion object {
        private const val LONG_TOKEN_LENGTH = 500
    }

    private val settings = MapSettings()
    private val cryptoHelper = CryptoHelper()
    private val tokenManager = SettingsTokenManager(settings, cryptoHelper)

    @Test
    fun getToken_returnsNull_whenNoTokenStored() {
        assertNull(tokenManager.getToken())
    }

    @Test
    fun setToken_and_getToken_roundTripsSuccessfully() {
        val token = AuthToken("test-jwt-token-12345")
        tokenManager.setToken(token)

        val retrieved = tokenManager.getToken()
        assertNotNull(retrieved)
        assertEquals("test-jwt-token-12345", retrieved.token)
    }

    @Test
    fun setToken_storesEncryptedValue_inSettings() {
        val token = AuthToken("my-secret-token")
        tokenManager.setToken(token)

        // The raw value in settings should NOT be the plain token
        val rawValue = settings.getStringOrNull("auth_token_encrypted")
        assertNotNull(rawValue)
        assertFalse(rawValue!!.contains("my-secret-token"))
    }

    @Test
    fun clear_removesTokenFromStorage() {
        tokenManager.setToken(AuthToken("token-to-clear"))
        assertNotNull(tokenManager.getToken())

        tokenManager.clear()
        assertNull(tokenManager.getToken())
    }

    @Test
    fun setToken_withNull_removesTokenFromStorage() {
        tokenManager.setToken(AuthToken("token-to-nullify"))
        assertNotNull(tokenManager.getToken())

        tokenManager.setToken(null)
        assertNull(tokenManager.getToken())
    }

    @Test
    fun setToken_overwritesPreviousToken() {
        tokenManager.setToken(AuthToken("first-token"))
        assertEquals("first-token", tokenManager.getToken()!!.token)

        tokenManager.setToken(AuthToken("second-token"))
        assertEquals("second-token", tokenManager.getToken()!!.token)
    }

    @Test
    fun getToken_handlesLongTokenSuccessfully() {
        val longToken = "a".repeat(LONG_TOKEN_LENGTH)
        tokenManager.setToken(AuthToken(longToken))

        val retrieved = tokenManager.getToken()
        assertNotNull(retrieved)
        assertEquals(longToken, retrieved.token)
    }

    @Test
    fun getToken_handlesSpecialCharactersInToken() {
        val specialToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0.abc123!@#$%"
        tokenManager.setToken(AuthToken(specialToken))

        val retrieved = tokenManager.getToken()
        assertNotNull(retrieved)
        assertEquals(specialToken, retrieved.token)
    }

    @Test
    fun multipleTokenManagers_withSameSettings_shareState() {
        val sharedSettings = MapSettings()
        val manager1 = SettingsTokenManager(sharedSettings, CryptoHelper())
        val manager2 = SettingsTokenManager(sharedSettings, CryptoHelper())

        manager1.setToken(AuthToken("shared-token"))
        assertEquals("shared-token", manager2.getToken()!!.token)
    }

    @Test
    fun isLoggedIn_returnsFalse_whenNoToken() {
        assertFalse(tokenManager.isLoggedIn())
    }

    @Test
    fun isLoggedIn_returnsTrue_whenTokenExists() {
        tokenManager.setToken(AuthToken("valid-token"))
        assertTrue(tokenManager.isLoggedIn())
    }

    private fun SettingsTokenManager.isAuthenticated(): Boolean = getToken() != null

    private fun SettingsTokenManager.isLoggedIn(): Boolean = isAuthenticated()
}
