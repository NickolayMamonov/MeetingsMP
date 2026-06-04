package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Краткая информация о событии.
 *
 * Используется в списках и превью. В отличие от [EventFull],
 * не содержит описания, статуса и других деталей.
 *
 * @property id Идентификатор события.
 * @property title Название события.
 * @property imageUrl URL изображения события.
 * @property date Дата проведения.
 * @property time Время проведения.
 * @property address Адрес проведения.
 * @property venueName Название места проведения.
 * @property metroStation Станция метро рядом с местом проведения.
 * @property attendeesCount Количество участников.
 * @property tags Теги / интересы события.
 * @property community Сообщество-организатор события.
 */
@Serializable
data class EventShort(
    val id: String,
    val title: String,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val date: String,
    val time: String,
    val address: String? = null,
    @SerialName("venueName") val venueName: String? = null,
    @SerialName("metroStation") val metroStation: String? = null,
    @SerialName("attendeesCount") val attendeesCount: Int,
    val tags: List<Interest> = emptyList(),
    val community: CommunityShort? = null
)

/**
 * Полная информация о событии.
 *
 * В отличие от [EventShort], содержит описание, информацию о спикере,
 * вместимость, статус, превью участников, связанные события
 * и статус регистрации текущего пользователя.
 *
 * @property id Идентификатор события.
 * @property title Название события.
 * @property description Описание события.
 * @property imageUrl URL изображения события.
 * @property date Дата проведения.
 * @property time Время проведения.
 * @property address Адрес проведения.
 * @property venueName Название места проведения.
 * @property metroStation Станция метро рядом с местом проведения.
 * @property capacity Вместимость площадки.
 * @property attendeesCount Количество участников.
 * @property status Статус события (предстоящее / прошедшее).
 * @property tags Теги / интересы события.
 * @property speaker Информация о спикере.
 * @property community Сообщество-организатор события.
 * @property attendeesPreview Превью участников события.
 * @property relatedEvents Связанные события.
 * @property isRegistered Флаг регистрации текущего пользователя.
 */
@Serializable
data class EventFull(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val date: String,
    val time: String,
    val address: String? = null,
    @SerialName("venueName") val venueName: String? = null,
    @SerialName("metroStation") val metroStation: String? = null,
    val capacity: Int? = null,
    @SerialName("attendeesCount") val attendeesCount: Int,
    val status: EventStatus,
    val tags: List<Interest> = emptyList(),
    val speaker: EventSpeaker? = null,
    val community: CommunityShort? = null,
    @SerialName("attendeesPreview") val attendeesPreview: List<UserShort> = emptyList(),
    @SerialName("relatedEvents") val relatedEvents: List<EventShort> = emptyList(),
    @SerialName("isRegistered") val isRegistered: Boolean = false
)

/**
 * Статус события.
 *
 * @property UPCOMING Предстоящее событие.
 * @property PAST Прошедшее событие.
 */
@Serializable
enum class EventStatus {
    UPCOMING, PAST
}

/**
 * Информация о спикере события.
 *
 * @property id Идентификатор спикера.
 * @property firstName Имя спикера.
 * @property bio Краткая биография спикера.
 * @property avatarUrl URL аватара спикера.
 */
@Serializable
data class EventSpeaker(
    val id: String,
    @SerialName("firstName") val firstName: String,
    val bio: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

/**
 * Регистрация пользователя на событие.
 *
 * @property eventId Идентификатор события.
 * @property registeredAt Дата и время регистрации.
 */
@Serializable
data class Registration(
    @SerialName("eventId") val eventId: String,
    @SerialName("registeredAt") val registeredAt: String
)

/**
 * Full event details including description, capacity, speaker, and registration status.
 *
 * @property id Unique identifier of the event.
 * @property title Event title.
 * @property description Optional detailed description of the event.
 * @property imageUrl Optional URL to the event's cover image.
 * @property date Event date (ISO 8601 or server-defined format).
 * @property time Event time (HH:mm or server-defined format).
 * @property address Optional street address of the event.
 * @property venueName Optional name of the venue.
 * @property metroStation Optional nearest metro station.
 * @property capacity Optional maximum number of attendees.
 * @property attendeesCount Current number of registered attendees.
 * @property status Current event status (UPCOMING or PAST).
 * @property tags List of interest tags associated with the event.
 * @property speaker Optional speaker information.
 * @property community Optional community that organizes the event.
 * @property attendeesPreview List of a few registered attendees for preview.
 * @property relatedEvents List of related events.
 * @property isRegistered Whether the current user is registered for this event.
 */
@Serializable
data class EventFull(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    val date: String,
    val time: String,
    val address: String? = null,
    @SerialName("venueName") val venueName: String? = null,
    @SerialName("metroStation") val metroStation: String? = null,
    val capacity: Int? = null,
    @SerialName("attendeesCount") val attendeesCount: Int,
    val status: EventStatus,
    val tags: List<Interest> = emptyList(),
    val speaker: EventSpeaker? = null,
    val community: CommunityShort? = null,
    @SerialName("attendeesPreview") val attendeesPreview: List<UserShort> = emptyList(),
    @SerialName("relatedEvents") val relatedEvents: List<EventShort> = emptyList(),
    @SerialName("isRegistered") val isRegistered: Boolean = false
)

/**
 * Status of an event.
 *
 * - [UPCOMING]: Event is scheduled for a future date.
 * - [PAST]: Event has already taken place.
 */
@Serializable
enum class EventStatus {
    UPCOMING, PAST
}

/**
 * Speaker information for an event.
 *
 * @property id Unique identifier of the speaker.
 * @property firstName First name of the speaker.
 * @property bio Optional short biography.
 * @property avatarUrl Optional URL to the speaker's avatar.
 */
@Serializable
data class EventSpeaker(
    val id: String,
    @SerialName("firstName") val firstName: String,
    val bio: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

/**
 * Registration record indicating a user's sign-up for an event.
 *
 * @property eventId Unique identifier of the event.
 * @property registeredAt Timestamp of when the registration was made.
 */
@Serializable
data class Registration(
    @SerialName("eventId") val eventId: String,
    @SerialName("registeredAt") val registeredAt: String
)