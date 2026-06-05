package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import org.koin.dsl.module

/**
 * DI module for application-level singletons.
 *
 * Provides the [MeetingsClient] instance, which is the central
 * API client for the application.
 */
val AppModule = module {
    single<MeetingsClient> { MeetingsClient(tokenProvider = get()) }
}
