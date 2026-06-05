package dev.whysoezzy.meetings.compose.ui.meetings

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Person

data class MeetingParticipantsUiState(
    val participants: List<Person> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
    val nextCursor: String? = null,
)
