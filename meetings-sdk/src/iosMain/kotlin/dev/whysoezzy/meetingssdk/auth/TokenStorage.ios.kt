@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetingssdk.auth

import com.russhwolf.settings.Settings

/**
 * iOS implementation of [TokenStorage] using multiplatform-settings.
 *
 * Tokens are stored via the platform-default Settings implementation,
 * which on iOS uses NSUserDefaults with the app sandbox providing
 * data isolation between apps.
 *
 * Note: A future improvement should use iOS Keychain Services
 * (SecItemAdd/SecItemCopyMatching) for hardware-backed encryption.
 * This requires Kotlin/Native interop with the Security framework
 * and is tracked as a separate task.
 *
 * @param platformContext Ignored on iOS (pass `null`).
 */
actual class TokenStorage actual constructor(platformContext: Any?) {

    private val settings: Settings = Settings()

    actual fun storeTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, accessToken)
        settings.putString(KEY_REFRESH_TOKEN, refreshToken)
    }

    actual fun getAccessToken(): String? =
        settings.getStringOrNull(KEY_ACCESS_TOKEN)

    actual fun getRefreshToken(): String? =
        settings.getStringOrNull(KEY_REFRESH_TOKEN)

    actual fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
    }
}
