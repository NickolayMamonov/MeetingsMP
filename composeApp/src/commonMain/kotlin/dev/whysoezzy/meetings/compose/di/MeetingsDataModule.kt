package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.data.MeetingsRepositoryImpl
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository
import org.koin.dsl.module

/**
 * DI module for meetings/events data layer.
 *
 * Provides [MeetingsRepository] backed by [MeetingsRepositoryImpl].
 */
val MeetingsDataModule = module {
    single<MeetingsRepository> { MeetingsRepositoryImpl(get()) }
}
