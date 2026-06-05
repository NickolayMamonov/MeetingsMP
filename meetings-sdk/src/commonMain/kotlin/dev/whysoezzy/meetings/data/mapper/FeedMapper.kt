package dev.whysoezzy.meetings.data.mapper

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.MeetingTag
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.TagState
import dev.whysoezzy.meetingssdk.models.CommunityShort
import dev.whysoezzy.meetingssdk.models.EventShort
import dev.whysoezzy.meetingssdk.models.FeedSectionType
import dev.whysoezzy.meetingssdk.models.FeedSections
import dev.whysoezzy.meetingssdk.models.Interest
import dev.whysoezzy.meetingssdk.models.UserShort

/**
 * Maps a list of [FeedSections] and pagination info to [MainScreenData].
 */
@Suppress("LongMethod")
fun List<FeedSections>.toMainScreenData(
    eventsNextCursor: String?,
    eventsTotal: Int,
): MainScreenData {
    var heroMeeting: Meeting? = null
    val nearestMeetings = mutableListOf<Meeting>()
    val recommendedCommunities = mutableListOf<Community>()
    val suggestedUsers = mutableListOf<Person>()
    val tags = mutableListOf<Tag>()
    val allMeetings = mutableListOf<Meeting>()

    for (section in this) {
        when (section.type) {
            FeedSectionType.EVENT_BANNER -> {
                heroMeeting = section.event?.toDomain()
            }

            FeedSectionType.NEAREST_EVENTS -> {
                section.events?.let { events ->
                    nearestMeetings.addAll(events.map { it.toDomain() })
                }
            }

            FeedSectionType.RECOMMENDED_COMMUNITIES -> {
                section.communities?.let { communities ->
                    recommendedCommunities.addAll(communities.map { it.toDomain() })
                }
            }

            FeedSectionType.SUGGESTED_USERS -> {
                section.users?.let { users ->
                    suggestedUsers.addAll(users.map { it.toDomain() })
                }
            }

            FeedSectionType.TAG_FILTERS -> {
                section.tags?.let { interests ->
                    tags.addAll(interests.map { it.toDomain() })
                }
            }

            FeedSectionType.EVENTS_LIST -> {
                section.events?.let { events ->
                    allMeetings.addAll(events.map { it.toDomain() })
                }
            }

            FeedSectionType.INTERESTS_CTA -> {
                // No data to map for interests CTA section
            }
        }
    }

    return MainScreenData(
        heroMeeting = heroMeeting,
        nearestMeetings = nearestMeetings,
        recommendedCommunities = recommendedCommunities,
        suggestedUsers = suggestedUsers,
        tags = tags,
        allMeetings = allMeetings,
        eventsNextCursor = eventsNextCursor,
        eventsTotal = eventsTotal,
    )
}

/**
 * Maps [EventShort] to domain [Meeting].
 */
fun EventShort.toDomain(): Meeting =
    Meeting(
        id = id.toLongOrNull() ?: 0L,
        imageUrl = imageUrl ?: "",
        title = title,
        description = "",
        time = 0L,
        date = date,
        address = dev.whysoezzy.meetings.domain.models.MeetingAddress(
            address = address ?: "",
            latitude = 0.0,
            longitude = 0.0,
        ),
        tags = tags.map { it.toDomainMeetingTag() },
        personHost = null,
        communityHost = community?.toCommunityHost(),
        participants = emptyList(),
        meetingStatus = dev.whysoezzy.meetings.domain.models.MeetingStatus.ACTIVE,
        isUserInParticipants = false,
        capacity = 0,
    )

/**
 * Maps [CommunityShort] to domain [Community].
 */
fun CommunityShort.toDomain(): Community = Community(
    id = id.toLongOrNull() ?: 0L,
    name = name,
    description = "",
    imageUrl = avatarUrl ?: "",
    subscribersCount = subscribersCount,
    isSubscribed = false,
    tags = emptyList(),
)

/**
 * Maps [Interest] to domain [Tag].
 */
fun Interest.toDomain(): Tag = Tag(
    id = id.toLongOrNull() ?: 0L,
    name = name,
)

/**
 * Maps [Interest] to domain [MeetingTag].
 */
fun Interest.toDomainMeetingTag(): MeetingTag = MeetingTag(
    id = id.toLongOrNull() ?: 0L,
    text = name,
    state = TagState.ACTIVE,
)

/**
 * Maps [UserShort] to domain [Person].
 */
fun UserShort.toDomain(): Person = Person(
    id = id.toLongOrNull() ?: 0L,
    name = firstName,
    surname = "",
    avatarUrl = avatarUrl ?: "",
    bio = "",
    role = "",
)

private fun CommunityShort.toCommunityHost(): dev.whysoezzy.meetings.domain.models.CommunityHost =
    dev.whysoezzy.meetings.domain.models.CommunityHost(
        id = id.toLongOrNull() ?: 0L,
        title = name,
        description = "",
        imageUrl = avatarUrl ?: "",
        meetingsInfo = emptyList(),
    )
