package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ad block returned by the events/ads endpoint.
 *
 * Represents a promotional block that can contain communities, text, or people.
 *
 * @property type Type of ad block: "COMMUNITIES", "TEXT", or "PEOPLE".
 * @property id Unique identifier of the ad block.
 * @property isActive Whether the ad block is currently active.
 * @property title Title of the ad block.
 * @property description Description of the ad block.
 * @property communities List of community previews (for COMMUNITIES type).
 * @property actionText Call-to-action text (for TEXT type).
 * @property actionUrl Call-to-action URL (for TEXT type).
 * @property users List of user previews (for PEOPLE type).
 */
@Serializable
data class AdBlock(
    val type: String,
    val id: String,
    @SerialName("isActive") val isActive: Boolean = true,
    val title: String,
    val description: String,
    val communities: List<CommunityShort>? = null,
    @SerialName("actionText") val actionText: String? = null,
    @SerialName("actionUrl") val actionUrl: String? = null,
    val users: List<UserShort>? = null,
)
