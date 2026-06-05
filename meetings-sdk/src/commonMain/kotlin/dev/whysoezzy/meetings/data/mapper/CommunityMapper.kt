package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetingssdk.models.dto.CommunityDto

fun CommunityDto.toDomain(): Community = Community(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    subscribersCount = subscribersCount,
    isSubscribed = isSubscribed,
    tags = tags.map { tagDto ->
        Tag(
            id = tagDto.id,
            name = tagDto.name,
        )
    },
)
