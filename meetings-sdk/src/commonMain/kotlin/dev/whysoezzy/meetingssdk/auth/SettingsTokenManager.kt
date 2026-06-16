package dev.whysoezzy.meetingssdk.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Persistent [TokenProvider] backed by [TokenStorage] for secure token persistence.
 *
 * Delegates all encryption and platform-specific storage to [TokenStorage],
 * which uses EncryptedSharedPreferences (Android), Keychain (iOS),
 * or AES-GCM encrypted file (Desktop).
 *
 * Provides reactive authentication state via [isLoggedIn] [StateFlow].
 *
 * @param tokenStorage The platform-specific secure storage implementation.
 */
class SettingsTokenManager(
    private val tokenStorage: TokenStorage,
) : TokenProvider {

    private val _isLoggedIn = MutableStateFlow(
        tokenStorage.getAccessToken() != null
    )
    override val isLoggedIn = _isLoggedIn.asStateFlow()

    override fun getAccessToken(): String? = tokenStorage.getAccessToken()

    override fun getRefreshToken(): String? = tokenStorage.getRefreshToken()

    override fun saveTokens(token: AuthToken) {
        tokenStorage.storeTokens(token.accessToken, token.refreshToken)
        _isLoggedIn.value = true
    }

    override fun clearTokens() {
        tokenStorage.clearTokens()
        _isLoggedIn.value = false
    }
}

