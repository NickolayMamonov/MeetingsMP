package dev.whysoezzy.meetings.compose.mapper

import dev.whysoezzy.meetings.compose.models.UIKitCommunity
import dev.whysoezzy.meetings.compose.models.UIKitMainScreenData
import dev.whysoezzy.meetings.compose.models.UIKitMeeting
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.models.UIKitTag
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.models.Tag

fun MainScreenData.toUIKit(): UIKitMainScreenData = UIKitMainScreenData(
    heroMeeting = heroMeeting?.let { meeting: Meeting -> meeting.toUIKit() },
    nearestMeetings = nearestMeetings.map { meeting: Meeting -> meeting.toUIKit() },
    recommendedCommunities = recommendedCommunities.map { community: Community -> community.toUIKit() },
    suggestedUsers = suggestedUsers.map { person: Person -> person.toUIKit() },
    tags = tags.map { tag: Tag -> tag.toUIKit() },
    allMeetings = allMeetings.map { meeting: Meeting -> meeting.toUIKit() },
    eventsNextCursor = eventsNextCursor,
    eventsTotal = eventsTotal,
)
