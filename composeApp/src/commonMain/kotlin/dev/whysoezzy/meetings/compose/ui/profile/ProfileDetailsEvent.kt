package dev.whysoezzy.meetings.compose.ui.profile

sealed interface ProfileDetailsEvent {
    data class Load(val userId: String) : ProfileDetailsEvent
    data object LoadCurrentUser : ProfileDetailsEvent
    data object Logout : ProfileDetailsEvent
    data object ClearError : ProfileDetailsEvent
}
