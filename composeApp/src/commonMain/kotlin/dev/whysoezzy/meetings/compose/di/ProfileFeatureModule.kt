package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.ProfileDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.ProfileEditViewModel
import dev.whysoezzy.meetings.domain.usecase.GetCurrentUserUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserByIdUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserCommunitiesUseCase
import dev.whysoezzy.meetings.domain.usecase.GetUserMeetingsUseCase
import dev.whysoezzy.meetings.domain.usecase.LogoutUseCase
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * DI module for profile feature.
 *
 * Provides use cases and ViewModels for profile details
 * and profile editing screens.
 */
val ProfileFeatureModule = module {
    // Use cases
    single { GetCurrentUserUseCase(get()) }
    single { GetUserByIdUseCase(get()) }
    single { GetUserMeetingsUseCase(get()) }
    single { GetUserCommunitiesUseCase(get()) }
    single { LogoutUseCase(get()) }

    // ViewModels
    viewModel { ProfileDetailsViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProfileEditViewModel(get(), get(), get()) }
}
