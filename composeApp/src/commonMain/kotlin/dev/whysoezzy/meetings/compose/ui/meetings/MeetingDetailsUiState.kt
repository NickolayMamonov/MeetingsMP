package dev.whysoezzy.meetings.compose.ui.meetings

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Meeting

data class MeetingDetailsUiState(
    val meeting: Meeting? = null,
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val isJoining: Boolean = false,
    val isLeaving: Boolean = false,
)
