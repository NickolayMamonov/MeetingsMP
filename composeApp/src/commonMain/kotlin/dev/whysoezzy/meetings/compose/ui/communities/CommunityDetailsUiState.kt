package dev.whysoezzy.meetings.compose.ui.communities

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting

data class CommunityDetailsUiState(
    val community: Community? = null,
    val meetings: List<Meeting> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val isSubscribing: Boolean = false,
)
