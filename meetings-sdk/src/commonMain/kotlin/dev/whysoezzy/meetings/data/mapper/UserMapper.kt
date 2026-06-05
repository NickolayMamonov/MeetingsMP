package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.MeetingInfo
import dev.whysoezzy.meetings.domain.models.MeetingStatus
import dev.whysoezzy.meetings.domain.models.SocialMediaInfo
import dev.whysoezzy.meetings.domain.models.SocialMediaType
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetingssdk.models.dto.MeetingInfoDto
import dev.whysoezzy.meetingssdk.models.dto.SocialMediaDto
import dev.whysoezzy.meetingssdk.models.dto.TagDto
import dev.whysoezzy.meetingssdk.models.dto.UpdateUserDto
import dev.whysoezzy.meetingssdk.models.dto.UserProfileDto
import kotlinx.datetime.Instant

fun UserProfileDto.toDomain(): User = User(
    id = id,
    name = name,
    surname = surname,
    email = email ?: "",
    city = city ?: "",
    avatar = avatarUrl ?: "",
    phone = phone ?: "",
    bio = description ?: "",
    socialMedias = socialMedias.map { it.toDomain() },
    interests = interests.map { it.toDomain() },
    showCommunities = showCommunities,
    showMeetings = showMeetings,
    notificationsEnabled = notificationsEnabled,
)

fun User.toUpdateDto(interestIds: List<Long>? = null): UpdateUserDto = UpdateUserDto(
    name = name.takeIf { it.isNotEmpty() },
    surname = surname.takeIf { it.isNotEmpty() },
    email = email.takeIf { it.isNotEmpty() },
    city = city.takeIf { it.isNotEmpty() },
    description = bio.takeIf { it.isNotEmpty() },
    avatarUrl = avatar.takeIf { it.isNotEmpty() },
    interestIds = interestIds,
    socialMedias = socialMedias.takeIf { it.isNotEmpty() }?.map { it.toDto() },
    showCommunities = showCommunities,
    showMeetings = showMeetings,
    notificationsEnabled = notificationsEnabled,
)

fun MeetingInfoDto.toMeetingInfo(): MeetingInfo = MeetingInfo(
    id = id,
    title = title,
    imageUrl = imageUrl,
    time = parseDateToTimestamp(date),
    address = "",
    tags = emptyList(),
    meetingStatus = MeetingStatus.ACTIVE,
)

private fun TagDto.toDomain(): Tag = Tag(
    id = id,
    name = name,
)

private fun SocialMediaDto.toDomain(): SocialMediaInfo = SocialMediaInfo(
    type = mapSocialMediaType(type),
    url = url,
    username = extractUsername(url),
)

private fun SocialMediaInfo.toDto(): SocialMediaDto = SocialMediaDto(
    type = type.name.lowercase(),
    url = url,
)

private fun mapSocialMediaType(platform: String): SocialMediaType =
    when (platform.uppercase()) {
        "TELEGRAM" -> SocialMediaType.TELEGRAM
        "HABR" -> SocialMediaType.HABR
        "GITHUB" -> SocialMediaType.GITHUB
        "LINKEDIN" -> SocialMediaType.LINKEDIN
        else -> SocialMediaType.TELEGRAM
    }

private fun extractUsername(url: String): String =
    url.substringAfterLast("/").takeIf { it.isNotBlank() } ?: url

private fun parseDateToTimestamp(dateString: String?): Long {
    if (dateString.isNullOrBlank()) return 0L
    return try {
        val normalized = dateString
            .replace(" ", "T")
            .replace("Z", "")
            .let { if (it.length == DATE_ONLY_LENGTH) "$it" + "T00:00:00" else it } +
            "+00:00"
        Instant.parse(normalized).toEpochMilliseconds()
    } catch (_: Exception) {
        0L
    }
}

private const val DATE_ONLY_LENGTH = 10
