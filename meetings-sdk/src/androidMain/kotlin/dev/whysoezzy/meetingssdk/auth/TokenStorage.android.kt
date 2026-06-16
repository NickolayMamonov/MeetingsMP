@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetingssdk.auth

import com.russhwolf.settings.Settings

/**
 * Android implementation of [TokenStorage].
 *
 * Tokens are stored in SharedPreferences with XOR obfuscation to prevent
 * plaintext reading from the device. The obfuscation key is deterministic
 * but the storage is app-private (MODE_PRIVATE).
 *
 * Note: A future improvement should use EncryptedSharedPreferences
 * (androidx.security.crypto) for hardware-backed AES-256 encryption
 * via AndroidKeyStore.
 * See: https://developer.android.com/topic/security/data
 *
 * @param platformContext Ignored on Android (Settings handles context internally).
 */
actual class TokenStorage actual constructor(platformContext: Any?) {

    private val settings: Settings = Settings()

    actual fun storeTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, obfuscate(accessToken))
        settings.putString(KEY_REFRESH_TOKEN, obfuscate(refreshToken))
    }

    actual fun getAccessToken(): String? {
        val encoded = settings.getStringOrNull(KEY_ACCESS_TOKEN) ?: return null
        return deobfuscateOrClear(encoded, KEY_ACCESS_TOKEN)
    }

    actual fun getRefreshToken(): String? {
        val encoded = settings.getStringOrNull(KEY_REFRESH_TOKEN) ?: return null
        return deobfuscateOrClear(encoded, KEY_REFRESH_TOKEN)
    }

    actual fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }

    private fun deobfuscateOrClear(encoded: String, key: String): String? {
        return try {
            deobfuscate(encoded)
        } catch (_: Exception) {
            settings.remove(key)
            null
        }
    }

    // ------- XOR obfuscation -------
    // Identical to the old CryptoHelper approach — prevents plaintext
    // reading from SharedPreferences / NSUserDefaults.

    private fun obfuscate(plaintext: String): String {
        val plainBytes = plaintext.encodeToByteArray()
        val encrypted = ByteArray(plainBytes.size) { i ->
            (plainBytes[i].toInt() xor keyByteAt(i)).toByte()
        }
        return encrypted.joinToString("") {
            (it.toInt() and BYTE_MASK).toString(HEX_RADIX).padStart(HEX_CHUNK_SIZE, '0')
        }
    }

    private fun deobfuscate(ciphertext: String): String {
        val encrypted = ciphertext.chunked(HEX_CHUNK_SIZE).map { it.toInt(HEX_RADIX).toByte() }.toByteArray()
        val decrypted = ByteArray(encrypted.size) { i ->
            (encrypted[i].toInt() xor keyByteAt(i)).toByte()
        }
        return decrypted.decodeToString()
    }

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val HEX_RADIX = 16
        const val HEX_CHUNK_SIZE = 2
        const val BYTE_MASK = 0xFF
        private val obfuscationKey = "M33tingsMP_S3cur3_K3y!2024".encodeToByteArray()

        fun keyByteAt(index: Int): Int =
            obfuscationKey[index % obfuscationKey.size].toInt() and BYTE_MASK
    }
}
