package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Краткая информация о пользователе.
 *
 * Используется в списках, превью и упоминаниях.
 * В отличие от [UserProfile], содержит только базовую информацию
 * без контактных данных, интересов и связанных сущностей.
 *
 * @property id Идентификатор пользователя.
 * @property firstName Имя пользователя.
 * @property avatarUrl URL аватара пользователя.
 */
@Serializable
data class UserShort(
    val id: String,
    @SerialName("firstName") val firstName: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

/**
 * Полный профиль пользователя.
 *
 * В отличие от [UserShort], содержит контактную информацию,
 * интересы, ссылки на соцсети, настройки приватности,
 * а также связанные события и сообщества.
 *
 * @property id Идентификатор пользователя.
 * @property firstName Имя пользователя.
 * @property phone Номер телефона.
 * @property city Город проживания.
 * @property bio Краткая информация о пользователе.
 * @property avatarUrl URL аватара пользователя.
 * @property interests Интересы пользователя.
 * @property socialLinks Ссылки на социальные сети.
 * @property showCommunities Флаг отображения сообществ в профиле.
 * @property showEvents Флаг отображения событий в профиле.
 * @property notificationsEnabled Флаг включения уведомлений.
 * @property events События пользователя.
 * @property communities Сообщества пользователя.
 */
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

/**
 * Ссылка на социальную сеть пользователя.
 *
 * @property platform Название платформы (например, "telegram", "github").
 * @property username Имя пользователя на платформе.
 */
@Serializable
data class SocialLink(
    val platform: String,
    val username: String
)

/**
 * Full user profile with detailed information.
 *
 * @property id Unique identifier of the user.
 * @property firstName First name of the user.
 * @property phone Optional phone number.
 * @property city Optional city of residence.
 * @property bio Optional short biography.
 * @property avatarUrl Optional URL to the user's avatar image.
 * @property interests List of interests associated with the user.
 * @property socialLinks List of social media links.
 * @property showCommunities Whether to show communities on the profile.
 * @property showEvents Whether to show events on the profile.
 * @property notificationsEnabled Whether push notifications are enabled.
 * @property events Optional list of events the user is associated with.
 * @property communities Optional list of communities the user belongs to.
 */
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

/**
 * A social media link associated with a user profile.
 *
 * @property platform Name of the social platform (e.g., "telegram", "instagram").
 * @property username Username or handle on that platform.
 */
@Serializable
data class SocialLink(
    val platform: String,
    val username: String
)