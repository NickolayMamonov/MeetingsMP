package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.data.AuthRepositoryImpl
import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.auth.SettingsTokenManager
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.auth.TokenStorage
import org.koin.dsl.module

/**
 * DI module for authentication data layer.
 *
 * Provides [TokenStorage] (platform-specific secure storage),
 * [TokenProvider] (persistent via [SettingsTokenManager]),
 * [AuthRepository] (backed by [AuthRepositoryImpl]).
 */
val AuthModule = module {
    single<TokenStorage> { TokenStorage() }
    single<TokenProvider> { SettingsTokenManager(tokenStorage = get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}

