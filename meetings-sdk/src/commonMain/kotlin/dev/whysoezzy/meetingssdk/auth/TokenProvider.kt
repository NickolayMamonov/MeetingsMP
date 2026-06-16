package dev.whysoezzy.meetingssdk.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Interface for managing authentication tokens.
 *
 * Implementations store tokens securely using platform-specific mechanisms
 * (Android EncryptedSharedPreferences, iOS Keychain, Desktop encrypted file).
 * Provides reactive access to authentication state via [isLoggedIn].
 */
interface TokenProvider {

    /**
     * Retrieves the current access token, or `null` if not authenticated.
     */
    fun getAccessToken(): String?

    /**
     * Retrieves the current refresh token, or `null` if not authenticated.
     */
    fun getRefreshToken(): String?

    /**
     * Stores both access and refresh tokens.
     * Updates [isLoggedIn] to `true`.
     *
     * @param token The [AuthToken] containing both access and refresh tokens.
     */
    fun saveTokens(token: AuthToken)

    /**
     * Clears the stored tokens (logs out).
     * Updates [isLoggedIn] to `false`.
     */
    fun clearTokens()

    /**
     * Reactive authentication state.
     * Emits `true` when tokens are present, `false` after [clearTokens].
     */
    val isLoggedIn: StateFlow<Boolean>
}

/**
 * Simple in-memory implementation of [TokenProvider].
 * Tokens are stored in volatile memory and are not persisted across app restarts.
 * Useful for testing and as a fallback when no persistent storage is available.
 */
class InMemoryTokenProvider : TokenProvider {

    private var accessToken: String? = null
    private var refreshToken: String? = null

    private val _isLoggedIn = MutableStateFlow(false)
    override val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    override fun getAccessToken(): String? = accessToken
    override fun getRefreshToken(): String? = refreshToken

    override fun saveTokens(token: AuthToken) {
        accessToken = token.accessToken
        refreshToken = token.refreshToken
        _isLoggedIn.value = true
    }

    override fun clearTokens() {
        accessToken = null
        refreshToken = null
        _isLoggedIn.value = false
    }
}

