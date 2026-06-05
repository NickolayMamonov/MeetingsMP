package dev.whysoezzy.meetingssdk.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(
    @SerialName("id") val id: Long,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("time") val time: Long,
    @SerialName("date") val date: String,
    @SerialName("address") val address: MeetingAddressDto,
    @SerialName("tags") val tags: List<MeetingTagDto>,
    @SerialName("personHost") val personHost: PersonHostDto?,
    @SerialName("communityHost") val communityHost: CommunityHostDto?,
    @SerialName("participants") val participants: List<PersonDto>,
    @SerialName("meetingStatus") val meetingStatus: String,
    @SerialName("isUserInParticipants") val isUserInParticipants: Boolean,
    @SerialName("capacity") val capacity: Int?,
)

@Serializable
data class MeetingAddressDto(
    @SerialName("address") val address: String,
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
)

@Serializable
data class MeetingTagDto(
    @SerialName("id") val id: Long,
    @SerialName("text") val text: String,
)

@Serializable
data class MeetingInfoDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("date") val date: String,
)

@Serializable
data class PersonHostDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("surname") val surname: String,
    @SerialName("description") val description: String,
    @SerialName("imageUrl") val imageUrl: String,
)

@Serializable
data class CommunityHostDto(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("imageUrl") val imageUrl: String,
    @SerialName("meetingsInfo") val meetingsInfo: List<MeetingInfoDto>,
)

@Serializable
data class PersonDto(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("surname") val surname: String,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("bio") val bio: String? = null,
    @SerialName("role") val role: String? = null,
)
