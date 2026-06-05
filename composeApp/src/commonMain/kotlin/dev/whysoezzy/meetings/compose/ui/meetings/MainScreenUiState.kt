package dev.whysoezzy.meetings.compose.ui.meetings

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.MainScreenData

data class MainScreenUiState(
    val data: MainScreenData? = null,
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val isRefreshing: Boolean = false,
)
