package dev.whysoezzy.meetings.compose.ui.auth

import dev.whysoezzy.meetings.common.error.ErrorType

data class PhoneInputUiState(
    val phone: String = "",
    val firstName: String = "",
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val retryAfterSeconds: Int? = null,
    val codeSent: Boolean = false,
)
