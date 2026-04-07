package dev.whysoezzy.meetingssdk.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedResponse(
    val sections: List<FeedSections>,
    @SerialName("eventsNextCursor") val eventsNextCursor: String? = null,
    @SerialName("eventsTotal") val eventsTotal: Int
)

@Serializable
data class FeedSections(
    val type: FeedSectionType,
    val title: String? = null,
    val event: EventShort? = null,
    val events: List<EventShort>? = null,
    val communities: List<CommunityShort>? = null,
    val users: List<UserShort>? = null,
    val tags: List<Interest>? = null
)

@Serializable
enum class FeedSectionType {
    EVENT_BANNER,
    NEAREST_EVENTS,
    INTERESTS_CTA,
    RECOMMENDED_COMMUNITIES,
    SUGGESTED_USERS,
    TAG_FILTERS,
    EVENTS_LIST
}
