package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.models.SocialMediaInfo
import dev.whysoezzy.meetings.domain.models.SocialMediaType
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetingssdk.models.CommunityShort
import dev.whysoezzy.meetingssdk.models.EventShort
import dev.whysoezzy.meetingssdk.models.Interest
import dev.whysoezzy.meetingssdk.models.SocialLink
import dev.whysoezzy.meetingssdk.models.UpdateUserBody
import dev.whysoezzy.meetingssdk.models.UserProfile

/**
 * Maps [UserProfile] (new API model) to domain [User].
 */
fun UserProfile.toDomainUser(): User = User(
    id = id.toLongOrNull() ?: 0L,
    name = firstName,
    surname = "",
    email = "",
    city = city ?: "",
    avatar = avatarUrl ?: "",
    phone = phone ?: "",
    bio = bio ?: "",
    socialMedias = socialLinks.map { it.toDomain() },
    interests = interests.map { it.toDomain() },
    showCommunities = showCommunities ?: true,
    showMeetings = showEvents ?: true,
    notificationsEnabled = notificationsEnabled ?: true,
)

/**
 * Maps [SocialLink] to domain [SocialMediaInfo].
 */
fun SocialLink.toDomain(): SocialMediaInfo = SocialMediaInfo(
    type = mapSocialMediaType(platform),
    url = "https://$platform.com/$username",
    username = username,
)

private fun mapSocialMediaType(platform: String): SocialMediaType =
    when (platform.uppercase()) {
        "TELEGRAM" -> SocialMediaType.TELEGRAM
        "HABR" -> SocialMediaType.HABR
        "GITHUB" -> SocialMediaType.GITHUB
        "LINKEDIN" -> SocialMediaType.LINKEDIN
        else -> SocialMediaType.TELEGRAM
    }

/**
 * Maps [Interest] (new API model) to domain [Tag].
 */
fun Interest.toDomainTag(): Tag = Tag(
    id = id.toLongOrNull() ?: 0L,
    name = name,
)

/**
 * Maps a list of [EventShort] (from user profile) to domain [Meeting] list.
 */
fun List<EventShort>.toDomainMeetings(): List<Meeting> =
    map { it.toDomain() }

/**
 * Maps a list of [CommunityShort] (from user profile) to domain [Community] list.
 */
fun List<CommunityShort>.toDomainCommunities(): List<Community> =
    map { it.toDomain() }

/**
 * Converts [User] domain model to [UpdateUserBody] for the new API layer.
 */
fun User.toUpdateUserBody(): UpdateUserBody = UpdateUserBody(
    firstName = name,
    city = city.ifEmpty { null },
    bio = bio.ifEmpty { null },
    showCommunities = showCommunities,
    showEvents = showMeetings,
    notificationsEnabled = notificationsEnabled,
)
