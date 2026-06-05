package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.data.CommunitiesRepositoryImpl
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository
import org.koin.dsl.module

/**
 * DI module for communities data layer.
 *
 * Provides [CommunitiesRepository] backed by [CommunitiesRepositoryImpl].
 */
val CommunitiesModule = module {
    single<CommunitiesRepository> { CommunitiesRepositoryImpl(get()) }
}
