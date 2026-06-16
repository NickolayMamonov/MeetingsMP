package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.CommunityHost
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.MeetingAddress
import dev.whysoezzy.meetings.domain.models.MeetingStatus
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetingssdk.models.CommunityShort
import dev.whysoezzy.meetingssdk.models.EventFull
import dev.whysoezzy.meetingssdk.models.EventSpeaker
import dev.whysoezzy.meetingssdk.models.EventStatus
import dev.whysoezzy.meetingssdk.models.UserShort

/**
 * Maps [EventFull] to domain [Meeting].
 */
@Suppress("LongMethod")
fun EventFull.toDomain(): Meeting =
    Meeting(
        id = id.toLongOrNull() ?: 0L,
        imageUrl = imageUrl ?: "",
        title = title,
        description = description ?: "",
        time = 0L,
        date = date,
        address = MeetingAddress(
            address = address ?: "",
            latitude = 0.0,
            longitude = 0.0,
        ),
        tags = tags.map { it.toDomainMeetingTag() },
        personHost = speaker?.toPersonHost(),
        communityHost = community?.toCommunityHost(),
        participants = attendeesPreview.map { it.toPerson() },
        meetingStatus = status.toDomain(),
        isUserInParticipants = isRegistered,
        capacity = capacity ?: 0,
        source = "TIMEPAD",
        externalUrl = null,
        isOnline = false,
    )

private fun EventStatus.toDomain(): MeetingStatus = when (this) {
    EventStatus.UPCOMING -> MeetingStatus.ACTIVE
    EventStatus.PAST -> MeetingStatus.COMPLETED
}

private fun EventSpeaker.toPersonHost(): dev.whysoezzy.meetings.domain.models.PersonHost =
    dev.whysoezzy.meetings.domain.models.PersonHost(
        id = id.toLongOrNull() ?: 0L,
        name = firstName,
        surname = "",
        description = bio ?: "",
        imageUrl = avatarUrl ?: "",
    )

private fun CommunityShort.toCommunityHost(): CommunityHost = CommunityHost(
    id = id.toLongOrNull() ?: 0L,
    title = name,
    description = "",
    imageUrl = avatarUrl ?: "",
    meetingsInfo = emptyList(),
)

private fun UserShort.toPerson(): Person = Person(
    id = id.toLongOrNull() ?: 0L,
    name = firstName,
    surname = "",
    avatarUrl = avatarUrl ?: "",
    bio = "",
    role = "",
)
