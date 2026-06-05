package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a user for display purposes.
 */
data class UIKitUser(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val city: String,
    val avatar: String,
    val phone: String,
    val bio: String,
    val socialMedias: List<UIKitSocialMedia> = emptyList(),
    val interests: List<UIKitTag> = emptyList(),
    val showCommunities: Boolean = true,
    val showMeetings: Boolean = true,
    val notificationsEnabled: Boolean = true,
)
