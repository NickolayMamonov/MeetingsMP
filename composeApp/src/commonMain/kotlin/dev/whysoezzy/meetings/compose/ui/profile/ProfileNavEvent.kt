package dev.whysoezzy.meetings.compose.ui.profile

sealed interface ProfileNavEvent {
    data object NavigateToEditProfile : ProfileNavEvent
    data object NavigateToLogin : ProfileNavEvent
    data object NavigateBack : ProfileNavEvent
}
