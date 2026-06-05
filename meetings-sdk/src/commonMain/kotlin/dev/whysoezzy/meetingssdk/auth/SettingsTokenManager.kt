package dev.whysoezzy.meetingssdk.auth

import com.russhwolf.settings.Settings
import dev.whysoezzy.meetings.common.crypto.CryptoHelper

/**
 * Persistent [TokenProvider] backed by multiplatform-settings with
 * platform-specific encryption via [CryptoHelper].
 *
 * Tokens are stored in an encrypted form so that they are not readable
 * in plain text from the settings storage. The encryption key is managed
 * by the platform-specific [CryptoHelper] implementation (Android Keystore,
 * desktop AES, iOS Keychain).
 *
 * @param settings The [Settings] instance to use for persistence.
 *   Defaults to [Settings] which resolves to the platform-default storage.
 * @param cryptoHelper The [CryptoHelper] used for encrypting/decrypting tokens.
 */
class SettingsTokenManager(
    private val settings: Settings = Settings(),
    private val cryptoHelper: CryptoHelper = CryptoHelper(),
) : TokenProvider {

    private companion object {
        const val KEY_AUTH_TOKEN = "auth_token_encrypted"
    }

    /**
     * Retrieves the current authentication token, decrypting it from storage.
     * Returns `null` if no token is stored or if decryption fails.
     */
    override fun getToken(): AuthToken? {
        val encrypted = settings.getStringOrNull(KEY_AUTH_TOKEN) ?: return null
        return try {
            val decrypted = cryptoHelper.decrypt(encrypted)
            AuthToken(decrypted)
        } catch (_: Exception) {
            // If decryption fails (e.g., key was rotated), clear the stale token
            clear()
            null
        }
    }

    /**
     * Stores a new authentication token, encrypting it before persisting.
     * Passing `null` removes the token from storage.
     */
    override fun setToken(token: AuthToken?) {
        if (token == null) {
            clear()
        } else {
            val encrypted = cryptoHelper.encrypt(token.token)
            settings.putString(KEY_AUTH_TOKEN, encrypted)
        }
    }

    /**
     * Removes the stored authentication token from persistent storage.
     */
    override fun clear() {
        settings.remove(KEY_AUTH_TOKEN)
    }
}
