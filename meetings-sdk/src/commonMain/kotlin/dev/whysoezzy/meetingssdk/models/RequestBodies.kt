package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Тело запроса на отправку кода подтверждения.
 *
 * @property phone Номер телефона пользователя.
 * @property firstName Имя пользователя.
 */
@Serializable
data class RequestCodeBody(
    val phone: String,
    @SerialName("firstName") val firstName: String
)

/**
 * Тело запроса на подтверждение кода аутентификации.
 *
 * @property phone Номер телефона пользователя.
 * @property code Код подтверждения, полученный по SMS.
 */
@Serializable
data class VerifyCodeBody(
    val phone: String,
    val code: String
)

/**
 * Тело запроса на обновление интересов пользователя.
 *
 * @property interestsIds Список идентификаторов интересов.
 */
@Serializable
data class UpdateInterestsBody(
    @SerialName("interestsIds") val interestsIds: List<String>
)

/**
 * Тело запроса на обновление профиля пользователя.
 *
 * Все поля опциональны — обновляются только переданные значения.
 *
 * @property firstName Имя пользователя.
 * @property city Город проживания.
 * @property bio Краткая информация о пользователе.
 * @property showCommunities Флаг отображения сообществ в профиле.
 * @property showEvents Флаг отображения событий в профиле.
 * @property notificationsEnabled Флаг включения уведомлений.
 * @property socialLinks Список ссылок на социальные сети.
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
 * Тело запроса на регистрацию устройства для push-уведомлений.
 *
 * @property token Токен устройства (FCM / APNs).
 * @property platform Платформа устройства (например, "android" или "ios").
 */
@Serializable
data class DeviceTokenBody(
    val token: String,
    val platform: String
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
    val platform: String
)