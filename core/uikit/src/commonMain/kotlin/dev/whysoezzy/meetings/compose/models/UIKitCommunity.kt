package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a community for display purposes.
 */
data class UIKitCommunity(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscribersCount: Int,
    val isSubscribed: Boolean,
    val tags: List<UIKitTag>,
)
