package dev.whysoezzy.meetings.compose.ui.auth

sealed interface AuthNavEvent {
    data object NavigateToCodeVerification : AuthNavEvent
    data object NavigateToNameInput : AuthNavEvent
    data object NavigateToMain : AuthNavEvent
    data object NavigateBack : AuthNavEvent
}
