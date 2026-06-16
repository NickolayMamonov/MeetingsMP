@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetingssdk.auth

import com.russhwolf.settings.Settings
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Desktop (JVM) implementation of [TokenStorage].
 *
 * Uses AES-GCM encrypted storage via multiplatform-settings with a
 * device-specific key derived from system properties (user home, OS name).
 * The encryption key is unique per user/machine without hardcoding a secret.
 *
 * This is less secure than hardware-backed storage (Android Keystore, iOS Keychain)
 * but provides reasonable protection for desktop apps. A future improvement could
 * use macOS Keychain CLI, Windows DPAPI, or Linux libsecret via JNA.
 *
 * @param platformContext Ignored on Desktop (pass `null`).
 */
actual class TokenStorage actual constructor(platformContext: Any?) {

    private companion object {
        const val KEY_ACCESS_TOKEN = "access_token"
        const val KEY_REFRESH_TOKEN = "refresh_token"
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_TAG_LENGTH = 128
        const val GCM_IV_LENGTH = 12
    }

    private val settings: Settings = Settings()
    private val secretKey: SecretKey by lazy { deriveDeviceKey() }

    actual fun storeTokens(accessToken: String, refreshToken: String) {
        settings.putString(KEY_ACCESS_TOKEN, encrypt(accessToken))
        settings.putString(KEY_REFRESH_TOKEN, encrypt(refreshToken))
    }

    actual fun getAccessToken(): String? {
        val encrypted = settings.getStringOrNull(KEY_ACCESS_TOKEN) ?: return null
        return decryptOrClear(encrypted, KEY_ACCESS_TOKEN)
    }

    actual fun getRefreshToken(): String? {
        val encrypted = settings.getStringOrNull(KEY_REFRESH_TOKEN) ?: return null
        return decryptOrClear(encrypted, KEY_REFRESH_TOKEN)
    }

    actual fun clearTokens() {
        settings.remove(KEY_ACCESS_TOKEN)
        settings.remove(KEY_REFRESH_TOKEN)
    }

    private fun decryptOrClear(encrypted: String, key: String): String? {
        return try {
            decrypt(encrypted)
        } catch (_: Exception) {
            // Decryption failed (key changed or data corrupted) — clear stale data
            settings.remove(key)
            null
        }
    }

    private fun deriveDeviceKey(): SecretKey {
        val seed = buildString {
            append(System.getProperty("user.home"))
            append(":")
            append(System.getProperty("user.name"))
            append(":")
            append(System.getProperty("os.name"))
            append(":meetings-desktop-v1")
        }
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val keyBytes = digest.digest(seed.encodeToByteArray())
        return SecretKeySpec(keyBytes, "AES")
    }

    private fun encrypt(plaintext: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plaintext.encodeToByteArray())
        val combined = iv + encrypted
        return Base64.getEncoder().encodeToString(combined)
    }

    private fun decrypt(ciphertext: String): String {
        val combined = Base64.getDecoder().decode(ciphertext)
        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encrypted = combined.copyOfRange(GCM_IV_LENGTH, combined.size)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decrypted = cipher.doFinal(encrypted)
        return decrypted.decodeToString()
    }
}

