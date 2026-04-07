package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommunityShort(
    val id: String,
    val name: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("subscribersCount") val subscribersCount: Int
)

data class CommunityFull(
    val id: String,
    val name: String,
    val description: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("subscribersCount") val subscribersCount: Int,
    val tags: List<Interest> = emptyList(),
    @SerialName("subscribersPreview") val subscribersPreview: List<UserShort> = emptyList(),
    @SerialName("upcomingEvents") val upcomingEvents: List<EventShort> = emptyList(),
    @SerialName("pastEvents") val pastEvents: PaginatedResponse<EventShort>,
    @SerialName("isSubscribed") val isSubscribed: Boolean = false
)

@Serializable
data class SubscriptionCount(
    @SerialName("subscribersCount") val subscribersCount: Int
)