package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.CodeVerificationViewModel
import dev.whysoezzy.meetings.compose.viewmodel.NameInputViewModel
import dev.whysoezzy.meetings.compose.viewmodel.PhoneInputViewModel
import dev.whysoezzy.meetings.domain.usecase.SendOtpUseCase
import dev.whysoezzy.meetings.domain.usecase.VerifyOtpUseCase
import dev.whysoezzy.meetings.domain.usecase.UpdateUserProfileUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * DI module for authentication feature.
 *
 * Provides use cases and ViewModels for the auth flow:
 * phone input → code verification → name input.
 */
val AuthFeatureModule = module {
    // Use cases
    single { SendOtpUseCase(get()) }
    single { VerifyOtpUseCase(get()) }
    single { UpdateUserProfileUseCase(get()) }

    // ViewModels
    viewModel { PhoneInputViewModel(get()) }
    viewModel { params -> CodeVerificationViewModel(params.get(), get()) }
    viewModel { NameInputViewModel(get()) }
}
