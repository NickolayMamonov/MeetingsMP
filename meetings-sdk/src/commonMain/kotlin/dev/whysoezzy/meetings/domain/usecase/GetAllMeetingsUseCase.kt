package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching all meetings from the feed.
 *
 * Returns the list of meetings from the EVENTS_LIST section of the feed.
 */
class GetAllMeetingsUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches all meetings from the feed.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of feed sections to return.
     * @param tags Optional comma-separated tag IDs to filter by.
     * @return [Result] with a list of [Meeting]s on success, or a failure.
     */
    suspend operator fun invoke(
        cursor: String? = null,
        limit: Int = 10,
        tags: String? = null,
    ): Result<List<Meeting>> =
        meetingsRepository.getMainScreenData(cursor, limit, tags).map { it.allMeetings }
}
