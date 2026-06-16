package dev.whysoezzy.meetingssdk.auth

/**
 * Platform-specific secure token storage.
 *
 * Provides obfuscated or encrypted persistence for access and refresh tokens
 * using platform-appropriate mechanisms:
 * - **Android**: XOR obfuscated SharedPreferences (planned: EncryptedSharedPreferences)
 * - **iOS**: XOR obfuscated NSUserDefaults (planned: Keychain Services)
 * - **Desktop**: AES-GCM encrypted file with OS-derived key
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

