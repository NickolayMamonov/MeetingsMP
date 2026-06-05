package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.CommunityInfo
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetingssdk.models.dto.CommunityInfoDto
import dev.whysoezzy.meetingssdk.models.dto.UserInfoDto

fun CommunityInfoDto.toDomain(): CommunityInfo = CommunityInfo(
    id = id,
    name = name,
    description = description ?: "",
    imageUrl = imageUrl,
    subscribersCount = subscribersCount ?: 0,
    isSubscribed = isSubscribed,
)

fun UserInfoDto.toDomain(): Person = Person(
    id = id,
    name = name,
    surname = surname,
    avatarUrl = avatarUrl,
    bio = bio,
    role = role,
)
