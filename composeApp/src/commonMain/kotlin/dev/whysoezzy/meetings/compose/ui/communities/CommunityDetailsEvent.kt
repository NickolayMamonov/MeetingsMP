package dev.whysoezzy.meetings.compose.ui.communities

sealed interface CommunityDetailsEvent {
    data class Load(val communityId: Long) : CommunityDetailsEvent
    data object Subscribe : CommunityDetailsEvent
    data object Unsubscribe : CommunityDetailsEvent
    data object ClearError : CommunityDetailsEvent
}
