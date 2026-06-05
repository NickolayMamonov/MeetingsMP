package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.AuthCheckViewModel
import dev.whysoezzy.meetings.domain.usecase.IsLoggedInUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

/**
 * DI module for cross-feature dependencies and navigation-level ViewModels.
 *
 * Provides [IsLoggedInUseCase] and [AuthCheckViewModel] used for
 * determining the initial navigation destination (auth vs main).
 */
val AppGlueModule = module {
    // Cross-feature use cases
    single { IsLoggedInUseCase(get()) }

    // Navigation ViewModel
    viewModel { AuthCheckViewModel(get()) }
}
