package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for sending an authentication code to a phone number.
 *
 * @property phone Phone number to send the code to.
 * @property firstName User's first name for registration purposes.
 */
@Serializable
data class RequestCodeBody(
    val phone: String,
    @SerialName("firstName") val firstName: String
)

/**
 * Request body for verifying a code sent to the user's phone.
 *
 * @property phone Phone number that received the code.
 * @property code The verification code to validate.
 */
@Serializable
data class VerifyCodeBody(
    val phone: String,
    val code: String
)

/**
 * Request body for updating the user's interest selections.
 *
 * @property interestsIds List of interest identifiers to set for the user.
 */
@Serializable
data class UpdateInterestsBody(
    @SerialName("interestsIds") val interestsIds: List<String>
)

/**
 * Request body for updating user profile fields.
 * All fields are optional — only provided fields will be updated.
 *
 * @property firstName New first name.
 * @property city New city of residence.
 * @property bio New short biography.
 * @property showCommunities Whether to show communities on the profile.
 * @property showEvents Whether to show events on the profile.
 * @property notificationsEnabled Whether push notifications are enabled.
 * @property socialLinks Updated list of social media links.
 */
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

/**
 * Request body for registering a device for push notifications.
 *
 * @property token The device token from the push notification service.
 * @property platform Platform identifier (e.g., "android", "ios").
 */
@Serializable
data class DeviceTokenBody(
    val token: String,
    val platform: String,
)

/**
 * Request body for refreshing an authentication token.
 *
 * @property token The current refresh token to exchange for a new access token.
 */
@Serializable
data class RefreshTokenBody(
    val token: String,
)
