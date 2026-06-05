package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing the main screen data for display purposes.
 */
data class UIKitMainScreenData(
    val heroMeeting: UIKitMeeting? = null,
    val nearestMeetings: List<UIKitMeeting> = emptyList(),
    val recommendedCommunities: List<UIKitCommunity> = emptyList(),
    val suggestedUsers: List<UIKitPerson> = emptyList(),
    val tags: List<UIKitTag> = emptyList(),
    val allMeetings: List<UIKitMeeting> = emptyList(),
    val eventsNextCursor: String? = null,
    val eventsTotal: Int = 0,
)
