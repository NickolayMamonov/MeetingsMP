package dev.whysoezzy.meetings.compose.ui.profile

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.User

data class ProfileDetailsUiState(
    val user: User? = null,
    val userMeetings: List<Meeting> = emptyList(),
    val userCommunities: List<Community> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
)
