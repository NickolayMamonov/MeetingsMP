package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing a generic host for display purposes.
 * Can represent either a person host or a community host.
 */
sealed interface UIKitHost {
    val id: Long
    val title: String
    val description: String
    val imageUrl: String

    data class Person(
        override val id: Long,
        override val title: String,
        override val description: String,
        override val imageUrl: String,
        val name: String,
        val surname: String,
    ) : UIKitHost

    data class Community(
        override val id: Long,
        override val title: String,
        override val description: String,
        override val imageUrl: String,
        val meetingsInfo: List<UIKitMeetingInfo>,
    ) : UIKitHost
}
