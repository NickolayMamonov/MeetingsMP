package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.MainScreenViewModel
import dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.MeetingParticipantsViewModel
import dev.whysoezzy.meetings.domain.usecase.GetAllMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetMainScreenDataUseCase
import dev.whysoezzy.meetings.domain.usecase.GetMeetingByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetMeetingParticipantsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetPopularMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetRecommendedCommunitiesUseCase
import dev.whysoezzy.meetings.domain.usecase.JoinMeetingUseCase
import dev.whysoezzy.meetings.domain.usecase.LeaveMeetingUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * DI module for main meetings/events feature.
 *
 * Provides use cases and ViewModels for the main screen,
 * meeting details, and meeting participants.
 */
val MainFeatureModule = module {
    // Use cases
    single { GetMainScreenDataUseCase(get()) }
    single { GetPopularMeetingsUseCase(get()) }
    single { GetAllMeetingsUseCase(get()) }
    single { GetMeetingByIdUseCase(get()) }
    single { GetMeetingParticipantsUseCase(get()) }
    single { JoinMeetingUseCase(get()) }
    single { LeaveMeetingUseCase(get()) }
    single { GetRecommendedCommunitiesUseCase(get()) }

    // ViewModels
    viewModel { MainScreenViewModel(get(), get(), get(), get()) }
    viewModel { MeetingDetailsViewModel(get(), get(), get()) }
    viewModel { MeetingParticipantsViewModel(get()) }
}
