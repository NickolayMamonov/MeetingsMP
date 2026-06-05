package dev.whysoezzy.meetings.compose.ui.auth

import dev.whysoezzy.meetings.common.error.ErrorType

data class NameInputUiState(
    val firstName: String = "",
    val lastName: String = "",
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
)
