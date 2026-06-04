package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Short representation of a community, used in lists and preview cards.
 *
 * @property id Unique identifier of the community.
 * @property name Name of the community.
 * @property avatarUrl Optional URL to the community's avatar.
 * @property subscribersCount Number of subscribers/members.
 */
@Serializable
data class CommunityShort(
    val id: String,
    val name: String,
    @SerialName("avatarUrl") val avatarUrl: String? = null,
    @SerialName("subscribersCount") val subscribersCount: Int
)

/**
 * Full community details including description, events, and subscription status.
 *
 * @property id Unique identifier of the community.
 * @property name Name of the community.
 * @property description Optional description of the community.
 * @property avatarUrl Optional URL to the community's avatar.
 * @property subscribersCount Number of subscribers/members.
 * @property tags List of interest tags associated with the community.
 * @property subscribersPreview List of a few subscribers for preview.
 * @property upcomingEvents List of upcoming events organized by the community.
 * @property pastEvents Paginated list of past events.
 * @property isSubscribed Whether the current user is subscribed to this community.
 */
@Serializable
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

/**
 * Response containing the updated subscriber count after a subscription action.
 *
 * @property subscribersCount Current number of subscribers.
 */
@Serializable
data class SubscriptionCount(
    @SerialName("subscribersCount") val subscribersCount: Int
)

/**
 * Полная информация о сообществе.
 *
 * В отличие от [CommunityShort], содержит описание, теги, список подписчиков,
 * предстоящие и прошедшие события, а также статус подписки текущего пользователя.
 *
 * @property id Идентификатор сообщества.
 * @property name Название сообщества.
 * @property description Описание сообщества.
 * @property avatarUrl URL аватара сообщества.
 * @property subscribersCount Количество подписчиков.
 * @property tags Теги / интересы сообщества.
 * @property subscribersPreview Превью подписчиков.
 * @property upcomingEvents Список предстоящих событий сообщества.
 * @property pastEvents Прошедшие события сообщества с пагинацией.
 * @property isSubscribed Флаг подписки текущего пользователя.
 */
@Serializable
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

/**
 * Количество подписчиков сообщества.
 *
 * @property subscribersCount Количество подписчиков.
 */
@Serializable
data class SubscriptionCount(
    @SerialName("subscribersCount") val subscribersCount: Int
)