package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetingssdk.models.CommunityFull
import dev.whysoezzy.meetingssdk.models.UserShort

/**
 * Maps [CommunityFull] (new API model) to domain [Community].
 */
fun CommunityFull.toDomainCommunity(): Community = Community(
    id = id.toLongOrNull() ?: 0L,
    name = name,
    description = description ?: "",
    imageUrl = avatarUrl ?: "",
    subscribersCount = subscribersCount,
    isSubscribed = isSubscribed,
    tags = tags.map { it.toDomain() },
)

/**
 * Maps a list of [UserShort] (from community subscribers) to domain [Person] list.
 */
fun List<UserShort>.toDomainPersons(): List<Person> =
    map { it.toDomain() }
