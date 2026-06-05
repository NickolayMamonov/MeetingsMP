package dev.whysoezzy.meetings.compose.ui.navigation

sealed interface AuthCheckNavEvent {
    data object NavigateToAuth : AuthCheckNavEvent
    data object NavigateToMain : AuthCheckNavEvent
}
