@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetings.common.crypto

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Desktop (JVM) implementation of [CryptoHelper] using AES-GCM with a derived key.
 *
 * On desktop, we derive a fixed AES-256 key from a stable application identifier.
 * This is less secure than Android Keystore (the key is deterministically derived,
 * not hardware-backed), but provides a reasonable level of protection for desktop apps.
 * The IV is randomly generated for each encryption and prepended to the ciphertext.
 */
actual class CryptoHelper actual constructor() {

    private companion object {
        const val TRANSFORMATION = "AES/GCM/NoPadding"
        const val GCM_TAG_LENGTH = 128
        const val GCM_IV_LENGTH = 12
        const val KEY_LENGTH_BITS = 256
        const val KEY_ALIAS = "meetings_auth_token_key_v1"
    }

    private val secretKey: SecretKey by lazy { deriveKey() }

    private fun deriveKey(): SecretKey {
        // Derive a stable AES-256 key from the key alias.
        // Uses SHA-256 to derive a deterministic key from the alias string.
        val keyDigest = java.security.MessageDigest.getInstance("SHA-256")
        val keyBytes = keyDigest.digest(KEY_ALIAS.toByteArray(Charsets.UTF_8))
        return SecretKeySpec(keyBytes, "AES")
    }

    actual fun encrypt(plaintext: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encrypted = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
        val combined = iv + encrypted
        return Base64.getEncoder().encodeToString(combined)
    }

    actual fun decrypt(ciphertext: String): String {
        val combined = Base64.getDecoder().decode(ciphertext)
        val iv = combined.copyOfRange(0, GCM_IV_LENGTH)
        val encrypted = combined.copyOfRange(GCM_IV_LENGTH, combined.size)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)
        val decrypted = cipher.doFinal(encrypted)
        return String(decrypted, Charsets.UTF_8)
    }
}
