package dev.whysoezzy.meetings.compose.mapper

import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.models.UIKitTag
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.CommunityInfo
import dev.whysoezzy.meetings.domain.models.Tag

fun Community.toUIKit(): UIKitCommunity = UIKitCommunity(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    subscribersCount = subscribersCount,
    isSubscribed = isSubscribed,
    tags = tags.map { it.toUIKit() },
)

fun CommunityInfo.toUIKit(): UIKitCommunityInfo = UIKitCommunityInfo(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    subscribersCount = subscribersCount,
    isSubscribed = isSubscribed,
)

fun Community.toUIKitInfo(): UIKitCommunityInfo = UIKitCommunityInfo(
    id = id,
    name = name,
    description = description,
    imageUrl = imageUrl,
    subscribersCount = subscribersCount,
    isSubscribed = isSubscribed,
)

fun Tag.toUIKit(): UIKitTag = UIKitTag(
    id = id,
    name = name,
)
