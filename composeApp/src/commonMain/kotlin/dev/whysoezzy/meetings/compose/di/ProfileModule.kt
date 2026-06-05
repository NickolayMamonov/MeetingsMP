package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.data.UsersRepositoryImpl
import dev.whysoezzy.meetings.domain.repository.UsersRepository
import org.koin.dsl.module

/**
 * DI module for user profile data layer.
 *
 * Provides [UsersRepository] backed by [UsersRepositoryImpl].
 */
val ProfileModule = module {
    single<UsersRepository> { UsersRepositoryImpl(get()) }
}
