package dev.whysoezzy.meetings.compose.ui.communities

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.domain.models.Person

data class CommunitySubscribersUiState(
    val subscribers: List<Person> = emptyList(),
    val isLoading: Boolean = false,
    val error: ErrorType? = null,
)
