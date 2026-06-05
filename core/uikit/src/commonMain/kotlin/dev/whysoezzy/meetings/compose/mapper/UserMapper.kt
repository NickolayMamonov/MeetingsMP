package dev.whysoezzy.meetings.compose.mapper

import dev.whysoezzy.meetings.compose.models.UIKitSocialMedia
import dev.whysoezzy.meetings.compose.models.UIKitSocialMediaType
import dev.whysoezzy.meetings.compose.models.UIKitUser
import dev.whysoezzy.meetings.domain.models.SocialMediaInfo
import dev.whysoezzy.meetings.domain.models.SocialMediaType
import dev.whysoezzy.meetings.domain.models.User

fun User.toUIKit(): UIKitUser = UIKitUser(
    id = id,
    name = name,
    surname = surname,
    email = email,
    city = city,
    avatar = avatar,
    phone = phone,
    bio = bio,
    socialMedias = socialMedias.map { it.toUIKit() },
    interests = interests.map { it.toUIKit() },
    showCommunities = showCommunities,
    showMeetings = showMeetings,
    notificationsEnabled = notificationsEnabled,
)

fun SocialMediaInfo.toUIKit(): UIKitSocialMedia = UIKitSocialMedia(
    type = type.toUIKit(),
    url = url,
    username = username,
)

fun SocialMediaType.toUIKit(): UIKitSocialMediaType = when (this) {
    SocialMediaType.TELEGRAM -> UIKitSocialMediaType.TELEGRAM
    SocialMediaType.HABR -> UIKitSocialMediaType.HABR
    SocialMediaType.LINKEDIN -> UIKitSocialMediaType.LINKEDIN
    SocialMediaType.GITHUB -> UIKitSocialMediaType.GITHUB
}
