package dev.whysoezzy.meetingssdk.auth

/**
 * Platform-specific secure token storage.
 *
 * Provides encrypted persistence for access and refresh tokens using
 * the most secure mechanism available on each platform:
 * - **Android**: EncryptedSharedPreferences (AES-256 via AndroidKeyStore)
 * - **iOS**: Keychain Services (kSecAttrAccessibleWhenUnlockedThisDeviceOnly)
 * - **Desktop**: Encrypted file with OS-derived key (fallback when native API unavailable)
 *
 * @param platformContext Platform-specific context. On Android, this must be a `android.content.Context`.
 *                        On iOS and Desktop, this parameter is ignored and can be `null`.
 */
expect class TokenStorage(platformContext: Any? = null) {

    /**
     * Stores both access and refresh tokens securely.
     * Overwrites any previously stored tokens.
     *
     * @param accessToken The JWT access token.
     * @param refreshToken The UUID refresh token.
     */
    fun storeTokens(accessToken: String, refreshToken: String)

    /**
     * Retrieves the stored access token, or `null` if not stored.
     */
    fun getAccessToken(): String?

    /**
     * Retrieves the stored refresh token, or `null` if not stored.
     */
    fun getRefreshToken(): String?

    /**
     * Removes all stored tokens (logout).
     */
    fun clearTokens()
}

