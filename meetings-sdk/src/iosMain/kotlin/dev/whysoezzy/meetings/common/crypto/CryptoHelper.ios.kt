@file:Suppress("MatchingDeclarationName", "MagicNumber")

package dev.whysoezzy.meetings.common.crypto

/**
 * iOS implementation of [CryptoHelper] using a simple XOR-based obfuscation.
 *
 * For production, this should be replaced with Keychain Services-based encryption
 * using the Security framework. The current implementation provides basic
 * obfuscation suitable for development and testing.
 *
 * On iOS, the actual secure storage should use Keychain directly via
 * expect/actual for the token storage itself, rather than encrypting
 * a settings value. This implementation serves as a placeholder.
 */
actual class CryptoHelper actual constructor() {

    private companion object {
        // Simple obfuscation key for development purposes.
        // Production iOS should use Keychain Services directly.
        const val OBFUSCATION_KEY = "M33tingsMP_S3cur3_K3y!2024"
    }

    actual fun encrypt(plaintext: String): String {
        val keyBytes = OBFUSCATION_KEY.encodeToByteArray()
        val plainBytes = plaintext.encodeToByteArray()
        val encrypted = ByteArray(plainBytes.size) { i ->
            (plainBytes[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
        }
        return encrypted.joinToString("") { (it.toInt() and 0xFF).toString(16).padStart(2, '0') }
    }

    actual fun decrypt(ciphertext: String): String {
        val keyBytes = OBFUSCATION_KEY.encodeToByteArray()
        val encrypted = ciphertext.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
        val decrypted = ByteArray(encrypted.size) { i ->
            (encrypted[i].toInt() xor keyBytes[i % keyBytes.size].toInt()).toByte()
        }
        return decrypted.decodeToString()
    }
}
