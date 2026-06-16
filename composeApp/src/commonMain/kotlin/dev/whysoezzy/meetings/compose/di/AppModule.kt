package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.network.CertificatePins
import org.koin.dsl.module

/**
 * DI module for application-level singletons.
 *
 * Provides the [MeetingsClient] instance, which is the central
 * API client for the application.
 *
 * Certificate pinning is disabled by default (empty [CertificatePins]).
 * To enable pinning in production, provide a [CertificatePins] instance
 * with SHA-256 hashes of your server's certificate SPKI:
 *
 * ```kotlin
 * single<CertificatePins> {
 *     CertificatePins(
 *         pins = mapOf(
 *             "api.meetings.mp" to listOf(
 *                 "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
 *             )
 *         )
 *     )
 * }
 * ```
 *
 * Pin hashes should be loaded from configuration (BuildConfig, local.properties),
 * never hardcoded in source code.
 */
val AppModule = module {
    single<CertificatePins> { CertificatePins() }
    single<MeetingsClient> { MeetingsClient(tokenProvider = get(), pins = get()) }
}
