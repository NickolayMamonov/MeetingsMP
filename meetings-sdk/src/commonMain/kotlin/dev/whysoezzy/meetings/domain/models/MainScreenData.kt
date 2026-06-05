package dev.whysoezzy.meetings.domain.models

/**
 * Domain model representing the main screen data.
 *
 * Contains all sections displayed on the main feed screen:
 * hero banner, nearest events, recommended communities, etc.
 */
data class MainScreenData(
    val heroMeeting: Meeting? = null,
    val nearestMeetings: List<Meeting> = emptyList(),
    val recommendedCommunities: List<Community> = emptyList(),
    val suggestedUsers: List<Person> = emptyList(),
    val tags: List<Tag> = emptyList(),
    val allMeetings: List<Meeting> = emptyList(),
    val eventsNextCursor: String? = null,
    val eventsTotal: Int = 0,
)
