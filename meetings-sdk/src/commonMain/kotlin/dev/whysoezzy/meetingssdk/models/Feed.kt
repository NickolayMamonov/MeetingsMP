package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Ответ сервера с лентой событий и рекомендаций.
 *
 * @property sections Список секций ленты.
 * @property eventsNextCursor Курсор для пагинации списка событий.
 * @property eventsTotal Общее количество событий в ленте.
 */
@Serializable
data class FeedResponse(
    val sections: List<FeedSections>,
    @SerialName("eventsNextCursor") val eventsNextCursor: String? = null,
    @SerialName("eventsTotal") val eventsTotal: Int
)

/**
 * Секция ленты событий и рекомендаций.
 *
 * Каждая секция имеет тип [FeedSectionType] и может содержать
 * событие, список событий, сообществ, пользователей или тегов.
 *
 * @property type Тип секции.
 * @property title Заголовок секции.
 * @property event Одиночное событие (для баннера).
 * @property events Список событий.
 * @property communities Список сообществ.
 * @property users Список пользователей.
 * @property tags Список тегов / интересов.
 */
@Serializable
data class FeedSections(
    val type: FeedSectionType,
    val title: String? = null,
    val event: EventShort? = null,
    val events: List<EventShort>? = null,
    val communities: List<CommunityShort>? = null,
    val users: List<UserShort>? = null,
    val tags: List<Interest>? = null
)

/**
 * Тип секции в ленте.
 *
 * @property EVENT_BANNER Баннер с одним событием.
 * @property NEAREST_EVENTS Ближайшие события.
 * @property INTERESTS_CTA Призыв к выбору интересов.
 * @property RECOMMENDED_COMMUNITIES Рекомендуемые сообщества.
 * @property SUGGESTED_USERS Рекомендуемые пользователи.
 * @property TAG_FILTERS Фильтры по тегам.
 * @property EVENTS_LIST Список событий.
 */
@Serializable
enum class FeedSectionType {
    EVENT_BANNER,
    NEAREST_EVENTS,
    INTERESTS_CTA,
    RECOMMENDED_COMMUNITIES,
    SUGGESTED_USERS,
    TAG_FILTERS,
    EVENTS_LIST
}
