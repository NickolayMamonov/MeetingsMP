package dev.whysoezzy.meetings.compose.di

import dev.whysoezzy.meetings.compose.viewmodel.AuthCheckViewModel
import dev.whysoezzy.meetings.compose.viewmodel.CodeVerificationViewModel
import dev.whysoezzy.meetings.compose.viewmodel.CommunityDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.CommunitySubscribersViewModel
import dev.whysoezzy.meetings.compose.viewmodel.MainScreenViewModel
import dev.whysoezzy.meetings.compose.viewmodel.MeetingDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.MeetingParticipantsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.NameInputViewModel
import dev.whysoezzy.meetings.compose.viewmodel.PhoneInputViewModel
import dev.whysoezzy.meetings.compose.viewmodel.ProfileDetailsViewModel
import dev.whysoezzy.meetings.compose.viewmodel.ProfileEditViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for Meetings app — defines ViewModel dependencies
 * consumed by the composeApp module.
 */
val meetingsModule = module {

    // ── Auth ViewModels ────────────────────────────────────────
    viewModelOf(::AuthCheckViewModel)
    viewModelOf(::PhoneInputViewModel)
    viewModel { params ->
        CodeVerificationViewModel(
            phone = params.get(),
            verifyOtpUseCase = get(),
        )
    }
    viewModelOf(::NameInputViewModel)

    // ── Meetings ViewModels ────────────────────────────────────
    viewModelOf(::MainScreenViewModel)
    viewModel { params ->
        MeetingDetailsViewModel(
            meetingId = params.get(),
            getMeetingByIdUseCase = get(),
            joinMeetingUseCase = get(),
            leaveMeetingUseCase = get(),
        )
    }
    viewModel { params ->
        MeetingParticipantsViewModel(
            meetingId = params.get(),
            getMeetingParticipantsUseCase = get(),
        )
    }

    // ── Communities ViewModels ─────────────────────────────────
    viewModel { params ->
        CommunityDetailsViewModel(
            communityId = params.get(),
            getCommunityByIdUseCase = get(),
            getCommunityMeetingsUseCase = get(),
            subscribeToCommunityUseCase = get(),
            unsubscribeFromCommunityUseCase = get(),
        )
    }
    viewModel { params ->
        CommunitySubscribersViewModel(
            communityId = params.get(),
            getCommunitySubscribersUseCase = get(),
        )
    }

    // ── Profile ViewModels ─────────────────────────────────────
    viewModelOf(::ProfileDetailsViewModel)
    viewModelOf(::ProfileEditViewModel)
}
