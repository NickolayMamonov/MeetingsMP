package dev.whysoezzy.meetings.compose.ui.communities

sealed interface CommunitiesNavEvent {
    data class NavigateToCommunityDetails(val communityId: Long) : CommunitiesNavEvent
    data class NavigateToCommunitySubscribers(val communityId: Long) : CommunitiesNavEvent
    data object NavigateBack : CommunitiesNavEvent
}
