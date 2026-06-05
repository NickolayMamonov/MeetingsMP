package dev.whysoezzy.meetings.compose.models

/**
 * UI model representing an advertisement block for display purposes.
 */
sealed interface UIKitAdBlock {
    val id: Long
    val isActive: Boolean
    val title: String
    val description: String

    data class CommunitiesAd(
        override val id: Long,
        override val title: String,
        override val description: String,
        val communities: List<UIKitCommunityInfo>,
        override val isActive: Boolean = true,
    ) : UIKitAdBlock

    data class TextAd(
        override val id: Long,
        override val title: String,
        override val description: String,
        val actionText: String? = null,
        val actionUrl: String? = null,
        override val isActive: Boolean = true,
    ) : UIKitAdBlock

    data class PeopleAd(
        override val id: Long,
        override val title: String,
        override val description: String,
        val users: List<UIKitPerson>,
        override val isActive: Boolean = true,
    ) : UIKitAdBlock
}
