package dev.whysoezzy.meetings.domain.repository

import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person

/**
 * Repository interface for meeting/event operations.
 *
 * Provides a clean domain-level API for fetching meetings,
 * searching, joining/leaving, and getting participants.
 */
interface MeetingsRepository {

    /**
     * Fetch the main screen data including hero banner, nearest events,
     * recommended communities, and tag filters.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of feed sections to return.
     * @param tags Optional comma-separated tag IDs to filter by.
     * @return [Result] with [MainScreenData] on success, or a failure.
     */
    suspend fun getMainScreenData(
        cursor: String? = null,
        limit: Int = 10,
        tags: String? = null,
    ): Result<MainScreenData>

    /**
     * Get a meeting by its unique identifier.
     *
     * @param id Unique identifier of the meeting.
     * @return [Result] with [Meeting] on success, or a failure.
     */
    suspend fun getMeetingById(id: String): Result<Meeting>

    /**
     * Search meetings by query text and optional tag filters.
     *
     * @param query Search query string.
     * @param tags Optional comma-separated tag IDs to filter by.
     * @param limit Maximum number of results to return.
     * @return [Result] with a list of matching [Meeting]s on success, or a failure.
     */
    suspend fun searchMeetings(
        query: String,
        tags: String? = null,
        limit: Int = 20,
    ): Result<List<Meeting>>

    /**
     * Get the list of participants for a specific meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of participants to return.
     * @return [Result] with a list of [Person]s on success, or a failure.
     */
    suspend fun getMeetingParticipants(
        meetingId: String,
        cursor: String? = null,
        limit: Int = 30,
    ): Result<List<Person>>

    /**
     * Register the current user for a meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend fun joinMeeting(meetingId: String): Result<Unit>

    /**
     * Unregister the current user from a meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend fun leaveMeeting(meetingId: String): Result<Unit>
}
