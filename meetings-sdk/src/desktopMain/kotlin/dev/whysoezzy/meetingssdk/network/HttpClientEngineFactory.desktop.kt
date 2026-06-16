@file:Suppress("MatchingDeclarationName")

package dev.whysoezzy.meetingssdk.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.CertificatePinner

/**
 * Desktop (JVM) implementation of [createHttpClientEngine].
 *
 * Uses the OkHttp engine with [CertificatePinner] when pins are provided
 * and [CertificatePins.enforcePins] is `true`.
 * When [pins] is `null`, empty, or `enforcePins` is `false`,
 * the engine is created without pinning (standard TLS validation still applies).
 */
actual fun createHttpClientEngine(pins: CertificatePins?): HttpClientEngine {
    if (pins != null && !pins.isEmpty && pins.enforcePins) {
        val pinnerBuilder = CertificatePinner.Builder()
        for ((hostname, pinList) in pins.pins) {
            for (pin in pinList) {
                pinnerBuilder.add(hostname, pin)
            }
        }
        val pinner = pinnerBuilder.build()
        return OkHttp.create {
            config {
                certificatePinner(pinner)
            }
        }
    }
    return OkHttp.create()
}
