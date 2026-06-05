package dev.whysoezzy.meetings.compose.ui.auth

import dev.whysoezzy.meetings.compose.models.UIKitUser

/**
 * Navigation events emitted by auth ViewModels.
 */
sealed interface AuthNavEvent {
    data object NavigateToCodeVerification : AuthNavEvent
    data object NavigateToNameInput : AuthNavEvent
    data object NavigateToMain : AuthNavEvent
    data object NavigateBack : AuthNavEvent
}

/**
 * Events for PhoneInputViewModel.
 */
sealed interface PhoneInputEvent {
    data class PhoneChanged(val phone: String) : PhoneInputEvent
    data class FirstNameChanged(val firstName: String) : PhoneInputEvent
    data object SendCode : PhoneInputEvent
}

/**
 * UI state for PhoneInputViewModel.
 */
data class PhoneInputUiState(
    val phone: String = "",
    val firstName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)

/**
 * Events for CodeVerificationViewModel.
 */
sealed interface CodeVerificationEvent {
    data class CodeChanged(val code: String) : CodeVerificationEvent
    data object VerifyCode : CodeVerificationEvent
}

/**
 * UI state for CodeVerificationViewModel.
 */
data class CodeVerificationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
) {
    val isValid: Boolean get() = code.length >= 4
}

/**
 * Events for NameInputViewModel.
 */
sealed interface NameInputEvent {
    data class FirstNameChanged(val firstName: String) : NameInputEvent
    data class LastNameChanged(val lastName: String) : NameInputEvent
    data object Save : NameInputEvent
}

/**
 * UI state for NameInputViewModel.
 */
data class NameInputUiState(
    val firstName: String = "",
    val lastName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
)
