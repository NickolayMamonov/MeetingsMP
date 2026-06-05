package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a meeting tag for display purposes.
 */
data class UIKitMeetingTag(
    val id: Long,
    val text: String,
    val state: UIKitTagState,
)

enum class UIKitTagState {
    ACTIVE,
    INACTIVE,
    SELECTED,
    DISABLED,
}
