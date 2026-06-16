package dev.whysoezzy.meetingssdk.network

import kotlinx.serialization.Serializable

/**
 * Configuration for TLS certificate pinning.
 *
 * Contains SHA-256 pin hashes for specific hosts. Pins are compared
 * against the server's certificate chain during the TLS handshake.
 * If a pin mismatch is detected, the connection is rejected.
 *
 * Pins should be provided from configuration (e.g., BuildConfig, local.properties),
 * never hardcoded in source code. This allows pin rotation without app updates.
 *
 * ## Pin format
 * Each pin is a base64-encoded SHA-256 hash of the certificate's subject public key info
 * (SPKI), prefixed with `sha256/`. Example:
 * ```
 * sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=
 * ```
 *
 * ## Pin rotation
 * Always include at least one backup pin. When rotating pins:
 * 1. Add the new pin alongside the current pin
 * 2. Deploy the app with both pins
 * 3. Once the app is widely adopted, remove the old pin
 *
 * @property pins Map of hostname to list of SHA-256 pin hashes.
 *   Key: hostname (e.g., "api.meetings.mp"),
 *   Value: list of acceptable pin hashes for that hostname.
 * @property enforcePins When `true`, connections to pinned hosts with mismatching
 *   certificates are rejected. When `false`, pin mismatches are logged but connections
 *   are allowed (useful for development/testing).
 */
@Serializable
data class CertificatePins(
    val pins: Map<String, List<String>> = emptyMap(),
    val enforcePins: Boolean = true,
) {
    /**
     * Returns `true` if no pins are configured.
     * When empty, certificate pinning is effectively disabled.
     */
    val isEmpty: Boolean get() = pins.isEmpty()

    /**
     * Returns the pin hashes for the given [hostname], or an empty list
     * if the hostname has no pins configured.
     */
    fun pinsFor(hostname: String): List<String> = pins[hostname] ?: emptyList()
}
