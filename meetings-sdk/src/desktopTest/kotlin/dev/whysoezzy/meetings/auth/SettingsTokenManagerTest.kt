package dev.whysoezzy.meetings.auth

import dev.whysoezzy.meetingssdk.auth.AuthToken
import dev.whysoezzy.meetingssdk.auth.SettingsTokenManager
import dev.whysoezzy.meetingssdk.auth.TokenStorage
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Tests for [SettingsTokenManager] using the Desktop [TokenStorage] implementation.
 *
 * The storage is cleared before each test to ensure clean test isolation
 * regardless of leftover state from previous test runs.
 */
@Suppress("FunctionNaming")
class SettingsTokenManagerTest {

    private val tokenStorage = TokenStorage()
    private val tokenManager = SettingsTokenManager(tokenStorage)

    @BeforeTest
    fun setUp() {
        // Ensure clean state before each test
        tokenStorage.clearTokens()
    }

    @Test
    fun getAccessToken_returnsNull_whenNoTokenStored() {
        assertNull(tokenManager.getAccessToken())
    }

    @Test
    fun getRefreshToken_returnsNull_whenNoTokenStored() {
        assertNull(tokenManager.getRefreshToken())
    }

    @Test
    fun saveTokens_and_getAccessToken_roundTripsSuccessfully() {
        tokenManager.saveTokens(
            AuthToken(accessToken = "test-access", refreshToken = "test-refresh")
        )

        assertEquals("test-access", tokenManager.getAccessToken())
        assertEquals("test-refresh", tokenManager.getRefreshToken())
    }

    @Test
    fun clearTokens_removesBothTokens() {
        tokenManager.saveTokens(
            AuthToken(accessToken = "access-to-clear", refreshToken = "refresh-to-clear")
        )
        assertTrue(tokenManager.isLoggedIn.value)

        tokenManager.clearTokens()
        assertNull(tokenManager.getAccessToken())
        assertNull(tokenManager.getRefreshToken())
        assertFalse(tokenManager.isLoggedIn.value)
    }

    @Test
    fun saveTokens_overwritesPreviousTokens() {
        tokenManager.saveTokens(
            AuthToken(accessToken = "first-access", refreshToken = "first-refresh")
        )
        assertEquals("first-access", tokenManager.getAccessToken())

        tokenManager.saveTokens(
            AuthToken(accessToken = "second-access", refreshToken = "second-refresh")
        )
        assertEquals("second-access", tokenManager.getAccessToken())
    }

    @Test
    fun isLoggedIn_reflectsTokenState() {
        assertFalse(tokenManager.isLoggedIn.value)
        tokenManager.saveTokens(AuthToken(accessToken = "a", refreshToken = "r"))
        assertTrue(tokenManager.isLoggedIn.value)
        tokenManager.clearTokens()
        assertFalse(tokenManager.isLoggedIn.value)
    }

    @Test
    fun saveTokens_updatesIsLoggedIn_toTrue() {
        assertFalse(tokenManager.isLoggedIn.value)
        tokenManager.saveTokens(AuthToken(accessToken = "a", refreshToken = "r"))
        assertTrue(tokenManager.isLoggedIn.value)
    }

    @Test
    fun clearTokens_updatesIsLoggedIn_toFalse() {
        tokenManager.saveTokens(AuthToken(accessToken = "a", refreshToken = "r"))
        assertTrue(tokenManager.isLoggedIn.value)
        tokenManager.clearTokens()
        assertFalse(tokenManager.isLoggedIn.value)
    }
}


