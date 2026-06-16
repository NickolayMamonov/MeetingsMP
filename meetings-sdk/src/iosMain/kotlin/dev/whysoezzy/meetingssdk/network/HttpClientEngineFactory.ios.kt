@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetingssdk.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

/**
 * iOS implementation of [createHttpClientEngine].
 *
 * Currently returns a standard Darwin engine without certificate pinning.
 *
 * iOS certificate pinning via `NSURLSession` delegate requires Kotlin/Native
 * interop with the Security framework (`SecTrustEvaluate`, `SecCertificateCopyData`)
 * and CommonCrypto (`CC_SHA256`) for SPKI hash computation. This is tracked as
 * a future improvement (see docs/certificate-pinning.md).
 *
 * Standard TLS certificate validation is still performed by the Darwin engine.
 *
 * @param pins Ignored on iOS in the current implementation. Will be used
 *   for pin validation in a future update.
 */
actual fun createHttpClientEngine(pins: CertificatePins?): HttpClientEngine {
    // NOTE: Certificate pinning for iOS using SecTrust + CommonCrypto is not yet
    //  implemented. This requires:
    //  1. Adding Security.framework and CommonCrypto to the iOS cinterop config
    //  2. Computing SHA-256 SPKI hashes of the server certificate chain
    //  3. Comparing against configured pins via handleChallenge
    //  For now, standard TLS validation is performed by the Darwin engine.
    return Darwin.create()
}
