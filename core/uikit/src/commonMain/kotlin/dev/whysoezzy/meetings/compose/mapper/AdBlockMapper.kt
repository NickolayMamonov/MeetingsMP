package dev.whysoezzy.meetings.compose.mapper

import dev.whysoezzy.meetings.compose.models.UIKitAdBlock
import dev.whysoezzy.meetings.compose.models.UIKitCommunityInfo
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.domain.models.AdBlock
import dev.whysoezzy.meetings.domain.models.CommunityInfo
import dev.whysoezzy.meetings.domain.models.Person

fun AdBlock.toUIKit(): UIKitAdBlock = when (this) {
    is AdBlock.CommunitiesAd -> UIKitAdBlock.CommunitiesAd(
        id = id,
        title = title,
        description = description,
        communities = communities.map { communityInfo: CommunityInfo ->
            communityInfo.toUIKit()
        },
        isActive = isActive,
    )
    is AdBlock.TextAd -> UIKitAdBlock.TextAd(
        id = id,
        title = title,
        description = description,
        actionText = actionText,
        actionUrl = actionUrl,
        isActive = isActive,
    )
    is AdBlock.PeopleAd -> UIKitAdBlock.PeopleAd(
        id = id,
        title = title,
        description = description,
        users = users.map { person: Person ->
            person.toUIKit()
        },
        isActive = isActive,
    )
}
