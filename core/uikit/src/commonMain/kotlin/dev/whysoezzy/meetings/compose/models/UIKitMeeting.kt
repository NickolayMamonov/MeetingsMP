package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a meeting for display purposes.
 *
 * This is a presentation-layer model that maps from the domain [Meeting] model.
 * It contains only the data needed for rendering the UI.
 */
data class UIKitMeeting(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val description: String,
    val time: Long,
    val date: String,
    val address: UIKitAddress,
    val tags: List<UIKitMeetingTag>,
    val personHost: UIKitPersonHost?,
    val communityHost: UIKitCommunityHost?,
    val participants: List<UIKitPerson>,
    val meetingStatus: UIKitStatus,
    val isUserInParticipants: Boolean,
    val capacity: Int,
    val source: String,
    val externalUrl: String?,
    val isOnline: Boolean,
    val hasLocation: Boolean,
)
