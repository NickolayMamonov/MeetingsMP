package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a social media link for display purposes.
 */
data class UIKitSocialMedia(
    val type: UIKitSocialMediaType,
    val url: String,
    val username: String,
)

enum class UIKitSocialMediaType {
    TELEGRAM,
    HABR,
    LINKEDIN,
    GITHUB,
}
