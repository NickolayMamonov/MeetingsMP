package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing brief community information for lists and cards.
 */
data class UIKitCommunityInfo(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val subscribersCount: Int,
    val isSubscribed: Boolean,
)
