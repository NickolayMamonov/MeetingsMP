package dev.whysoezzy.meetingssdk.network

import io.ktor.client.engine.HttpClientEngine

/**
 * Creates a platform-specific [HttpClientEngine] with optional certificate pinning.
 *
 * On Android and Desktop (JVM), this returns an OkHttp engine configured with
 * [okhttp3.CertificatePinner] when [pins] are provided and [CertificatePins.enforcePins]
 * is `true`.
 *
 * On iOS, this returns a standard Darwin engine without certificate pinning
 * (pinning requires Kotlin/Native cinterop with Security framework — not yet implemented).
 *
 * When [pins] is `null`, empty, or `enforcePins` is `false`, the engine is created
 * without pinning (standard TLS validation still applies).
 *
 * @param pins Certificate pin configuration. Pass `null` to disable pinning.
 * @return A configured [HttpClientEngine] for the current platform.
 */
expect fun createHttpClientEngine(pins: CertificatePins?): HttpClientEngine
