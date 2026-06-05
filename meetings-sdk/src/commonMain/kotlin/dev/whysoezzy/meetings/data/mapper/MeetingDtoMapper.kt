package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.CommunityHost
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.MeetingAddress
import dev.whysoezzy.meetings.domain.models.MeetingInfo
import dev.whysoezzy.meetings.domain.models.MeetingStatus
import dev.whysoezzy.meetings.domain.models.MeetingTag
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.models.PersonHost
import dev.whysoezzy.meetings.domain.models.TagState
import dev.whysoezzy.meetingssdk.models.dto.MeetingDto

@Suppress("LongMethod")
fun MeetingDto.toDomain(): Meeting =
    Meeting(
        id = id,
        imageUrl = imageUrl,
        title = title,
        description = description,
        time = time,
        date = date,
        address =
            MeetingAddress(
                address = address.address,
                latitude = address.latitude,
                longitude = address.longitude,
            ),
        tags =
            tags.map { tagDto ->
                MeetingTag(
                    id = tagDto.id,
                    text = tagDto.text,
                    state = TagState.ACTIVE,
                )
            },
        personHost =
            personHost?.let { host ->
                PersonHost(
                    id = host.id,
                    name = host.name,
                    surname = host.surname,
                    description = host.description,
                    imageUrl = host.imageUrl,
                )
            },
        communityHost =
            communityHost?.let { host ->
                CommunityHost(
                    id = host.id,
                    title = host.title,
                    description = host.description,
                    imageUrl = host.imageUrl,
                    meetingsInfo =
                        host.meetingsInfo.map { infoDto ->
                            MeetingInfo(
                                id = infoDto.id,
                                title = infoDto.title,
                                imageUrl = infoDto.imageUrl,
                                time = 0L,
                                tags = emptyList(),
                                address = "",
                                meetingStatus = MeetingStatus.ACTIVE,
                            )
                        },
                )
            },
        participants =
            participants.map { personDto ->
                Person(
                    id = personDto.id,
                    name = personDto.name,
                    surname = personDto.surname,
                    avatarUrl = personDto.imageUrl ?: "",
                    bio = personDto.bio ?: "",
                    role = personDto.role?.takeIf { it.isNotBlank() } ?: "Не указано",
                )
            },
        meetingStatus = meetingStatus.toMeetingStatus(),
        isUserInParticipants = isUserInParticipants,
        capacity = capacity ?: 0,
    )

fun String.toMeetingStatus(): MeetingStatus =
    when (uppercase().trim()) {
        "ACTIVE" -> MeetingStatus.ACTIVE
        "COMPLETED", "FINISHED" -> MeetingStatus.COMPLETED
        "CANCELLED" -> MeetingStatus.CANCELLED
        "FULL" -> MeetingStatus.FULL
        "DRAFT" -> MeetingStatus.DRAFT
        else -> MeetingStatus.ACTIVE
    }
