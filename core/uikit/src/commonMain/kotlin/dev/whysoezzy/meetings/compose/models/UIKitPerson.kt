package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a person for display purposes.
 */
data class UIKitPerson(
    val id: Long,
    val name: String,
    val surname: String,
    val avatarUrl: String,
    val bio: String,
    val role: String,
)
