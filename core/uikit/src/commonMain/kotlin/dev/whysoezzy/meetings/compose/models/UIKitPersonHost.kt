package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a person host for display purposes.
 */
data class UIKitPersonHost(
    val id: Long,
    val name: String,
    val surname: String,
    val description: String,
    val imageUrl: String,
)
