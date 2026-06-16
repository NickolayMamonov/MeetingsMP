package dev.whysoezzy.meetingssdk.network

import io.ktor.client.engine.HttpClientEngine

/**
 * Creates a platform-specific [HttpClientEngine] with optional certificate pinning.
 *
 * On Android and Desktop (JVM), this returns an OkHttp engine configured with
 * [okhttp3.CertificatePinner] if [pins] are provided.
 *
 * On iOS, this returns a Darwin engine configured with a custom
 * `NSURLSession` delegate that validates certificate pins.
 *
 * When [pins] is `null` or empty, the engine is created without pinning
 * (standard TLS validation still applies).
 *
 * @param pins Certificate pin configuration. Pass `null` to disable pinning.
 * @return A configured [HttpClientEngine] for the current platform.
 */
expect fun createHttpClientEngine(pins: CertificatePins?): HttpClientEngine
