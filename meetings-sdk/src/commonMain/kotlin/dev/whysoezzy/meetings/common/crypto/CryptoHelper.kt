package dev.whysoezzy.meetings.common.crypto

/**
 * Platform-specific cryptographic helper for encrypting/decrypting sensitive data.
 *
 * On Android, this uses Android Keystore with AEAD.
 * On Desktop, this uses AES-GCM with a locally stored key.
 * On iOS, this uses Keychain-based storage.
 */
expect class CryptoHelper() {

    /**
     * Encrypts the given plaintext string and returns a Base64-encoded ciphertext.
     *
     * @param plaintext The string to encrypt.
     * @return Base64-encoded encrypted string.
     */
    fun encrypt(plaintext: String): String

    /**
     * Decrypts a Base64-encoded ciphertext string back to the original plaintext.
     *
     * @param ciphertext Base64-encoded encrypted string.
     * @return The original decrypted string.
     */
    fun decrypt(ciphertext: String): String
}
