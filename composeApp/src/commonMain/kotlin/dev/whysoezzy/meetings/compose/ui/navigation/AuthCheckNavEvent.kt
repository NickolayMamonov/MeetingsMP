package dev.whysoezzy.meetings.compose.ui.navigation

/**
 * Navigation events emitted by [AuthCheckViewModel] to signal
 * where the splash screen should navigate after checking auth state.
 */
sealed interface AuthCheckNavEvent {
    data object NavigateToAuth : AuthCheckNavEvent
    data object NavigateToMain : AuthCheckNavEvent
}
