package dev.whysoezzy.meetings.compose.ui.communities

sealed interface CommunitySubscribersEvent {
    data class Load(val communityId: Long) : CommunitySubscribersEvent
    data object ClearError : CommunitySubscribersEvent
}
