package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

@Serializable
enum class EventStatus {
    UPCOMING, PAST
}

@Serializable
data class EventSpeaker(
    val id: String,
    @SerialName("firstName") val firstName: String,
    val bio: String? = null,
    @SerialName("avatarUrl") val avatarUrl: String? = null
)

@Serializable
data class Registration(
    @SerialName("eventId") val eventId: String,
    @SerialName("registeredAt") val registeredAt: String
)