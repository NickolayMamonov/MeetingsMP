package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.CommunityDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.CommunitySubscribersViewModel
import dev.whysoezzy.meetings.domain.usecase.GetCommunityByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetCommunityMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.GetCommunitySubscribersUseCase
import dev.whysoezzy.meetings.domain.usecase.SubscribeToCommunityUseCase
import dev.whysoezzy.meetings.domain.usecase.UnsubscribeFromCommunityUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * DI module for communities feature.
 *
 * Provides use cases and ViewModels for community details
 * and community subscribers screens.
 */
val CommunityModule = module {
    // Use cases
    single { GetCommunityByIdUseCase(get()) }
    single { GetCommunityMeetingsUseCase(get()) }
    single { GetCommunitySubscribersUseCase(get()) }
    single { SubscribeToCommunityUseCase(get()) }
    single { UnsubscribeFromCommunityUseCase(get()) }

    // ViewModels
    viewModel { CommunityDetailsViewModel(get(), get(), get(), get()) }
    viewModel { CommunitySubscribersViewModel(get()) }
}
