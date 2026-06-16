package dev.whysoezzy.meetings.domain.models

data class Meeting(
    val id: Long,
    val imageUrl: String,
    val title: String,
    val description: String,
    val time: Long,
    val date: String,
    val address: MeetingAddress,
    val tags: List<MeetingTag>,
    val personHost: PersonHost?,
    val communityHost: CommunityHost?,
    val participants: List<Person>,
    val meetingStatus: MeetingStatus,
    val isUserInParticipants: Boolean,
    val capacity: Int,
    val source: String,
    val externalUrl: String?,
    val isOnline: Boolean,
) {
    val hasLocation: Boolean get() = !isOnline && address.latitude != 0.0 && address.longitude != 0.0
}

data class MeetingAddress(
    val address: String,
    val latitude: Double,
    val longitude: Double,
)

data class PersonHost(
    val id: Long,
    val name: String,
    val surname: String,
    val description: String,
    val imageUrl: String,
)

data class CommunityHost(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val meetingsInfo: List<MeetingInfo>,
)
