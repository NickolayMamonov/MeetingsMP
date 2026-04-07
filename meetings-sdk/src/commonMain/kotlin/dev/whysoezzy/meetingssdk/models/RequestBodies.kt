package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestCodeBody(
    val phone: String,
    @SerialName("firstName") val firstName: String
)

@Serializable
data class VerifyCodeBody(
    val phone: String,
    val code: String
)

@Serializable
data class UpdateInterestsBody(
    @SerialName("interestsIds") val interestsIds: List<String>
)

@Serializable
data class UpdateUserBody(
    @SerialName("firstName") val firstName: String? = null,
    val city: String? = null,
    val bio: String? = null,
    @SerialName("showCommunities") val showCommunities: Boolean? = null,
    @SerialName("showEvents") val showEvents: Boolean? = null,
    @SerialName("notificationEnabled") val notificationsEnabled: Boolean? = null,
    @SerialName("socialLinks") val socialLinks: List<SocialLink>? = null
)

@Serializable
data class DeviceTokenBody(
    val token: String,
    val platform: String
)