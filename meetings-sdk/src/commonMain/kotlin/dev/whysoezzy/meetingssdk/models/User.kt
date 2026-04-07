package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserShort(
    val id: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

@Serializable
data class UserProfile(
    val id: String,
    val firstName: String,
    val phone: String? = null,
    val city: String? = null,
    val bio: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    val interests: List<Interest> = emptyList(),
    @SerialName("socialLinks") val socialLinks: List<SocialLink> = emptyList(),
    val showCommunities: Boolean? = null,
    val showEvents: Boolean? = null,
    val notificationsEnabled: Boolean? = null,
    val events: List<EventShort>? = null,
    val communities: List<CommunityShort>? = null
)


@Serializable
data class SocialLink(
    val platform: String,
    val username: String
)