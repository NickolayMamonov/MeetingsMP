package dev.whysoezzy.meetings.compose.ui.auth

import dev.whysoezzy.meetings.common.error.ErrorType

data class CodeVerificationUiState(
    val code: String = "",
    val isValid: Boolean = false,
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val isVerified: Boolean = false,
)
