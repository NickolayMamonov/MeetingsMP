package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a community host for display purposes.
 */
data class UIKitCommunityHost(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val meetingsInfo: List<UIKitMeetingInfo>,
)
