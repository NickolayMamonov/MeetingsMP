package dev.whysoezzy.meetings.compose.ui.auth

sealed interface CodeVerificationEvent {
    data class CodeChanged(val code: String) : CodeVerificationEvent
    data object VerifyCode : CodeVerificationEvent
    data object ClearError : CodeVerificationEvent
}
