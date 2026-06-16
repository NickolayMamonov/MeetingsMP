package dev.whysoezzy.meetings.compose.mapper

import dev.whysoezzy.meetings.compose.models.UIKitAddress
import dev.whysoezzy.meetings.compose.models.UIKitCommunityHost
import dev.whysoezzy.meetings.compose.models.UIKitMeeting
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.models.UIKitMeetingTag
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.models.UIKitPersonHost
import dev.whysoezzy.meetings.compose.models.UIKitStatus
import dev.whysoezzy.meetings.compose.models.UIKitTagState
import dev.whysoezzy.meetings.domain.models.CommunityHost
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.MeetingAddress
import dev.whysoezzy.meetings.domain.models.MeetingInfo
import dev.whysoezzy.meetings.domain.models.MeetingStatus
import dev.whysoezzy.meetings.domain.models.MeetingTag
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.models.PersonHost
import dev.whysoezzy.meetings.domain.models.TagState

fun Meeting.toUIKit(): UIKitMeeting = UIKitMeeting(
    id = id,
    imageUrl = imageUrl,
    title = title,
    description = description,
    time = time,
    date = date,
    address = address.toUIKit(),
    tags = tags.map { it.toUIKit() },
    personHost = personHost?.toUIKit(),
    communityHost = communityHost?.toUIKit(),
    participants = participants.map { it.toUIKit() },
    meetingStatus = meetingStatus.toUIKit(),
    isUserInParticipants = isUserInParticipants,
    capacity = capacity,
    source = source,
    externalUrl = externalUrl,
    isOnline = isOnline,
    hasLocation = hasLocation,
)

fun MeetingAddress.toUIKit(): UIKitAddress = UIKitAddress(
    address = address,
    latitude = latitude,
    longitude = longitude,
)

fun MeetingTag.toUIKit(): UIKitMeetingTag = UIKitMeetingTag(
    id = id,
    text = text,
    state = state.toUIKit(),
)

fun TagState.toUIKit(): UIKitTagState = when (this) {
    TagState.ACTIVE -> UIKitTagState.ACTIVE
    TagState.INACTIVE -> UIKitTagState.INACTIVE
    TagState.SELECTED -> UIKitTagState.SELECTED
    TagState.DISABLED -> UIKitTagState.DISABLED
}

fun PersonHost.toUIKit(): UIKitPersonHost = UIKitPersonHost(
    id = id,
    name = name,
    surname = surname,
    description = description,
    imageUrl = imageUrl,
)

fun CommunityHost.toUIKit(): UIKitCommunityHost = UIKitCommunityHost(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    meetingsInfo = meetingsInfo.map { it.toUIKit() },
)

fun Person.toUIKit(): UIKitPerson = UIKitPerson(
    id = id,
    name = name,
    surname = surname,
    avatarUrl = avatarUrl,
    bio = bio,
    role = role,
)

fun MeetingStatus.toUIKit(): UIKitStatus = when (this) {
    MeetingStatus.ACTIVE -> UIKitStatus.ACTIVE
    MeetingStatus.COMPLETED -> UIKitStatus.COMPLETED
    MeetingStatus.CANCELLED -> UIKitStatus.CANCELLED
    MeetingStatus.FULL -> UIKitStatus.FULL
    MeetingStatus.DRAFT -> UIKitStatus.DRAFT
}

fun MeetingInfo.toUIKit(): UIKitMeetingInfo = UIKitMeetingInfo(
    id = id,
    imageUrl = imageUrl,
    title = title,
    address = address,
    tags = tags.map { it.toUIKit() },
    time = time,
    meetingStatus = meetingStatus.toUIKit(),
)

fun Meeting.toUIKitInfo(): UIKitMeetingInfo = UIKitMeetingInfo(
    id = id,
    imageUrl = imageUrl,
    title = title,
    address = address.address,
    tags = tags.map { it.toUIKit() },
    time = time,
    meetingStatus = meetingStatus.toUIKit(),
)
