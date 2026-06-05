package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing brief meeting information for lists and cards.
 */
data class UIKitMeetingInfo(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val address: String,
    val tags: List<UIKitMeetingTag>,
    val time: Long,
    val meetingStatus: UIKitStatus,
)
