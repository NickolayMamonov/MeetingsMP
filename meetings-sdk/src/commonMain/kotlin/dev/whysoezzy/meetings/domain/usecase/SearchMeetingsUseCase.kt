package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for searching meetings by query text and optional tag filters.
 */
class SearchMeetingsUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Searches meetings matching the given query.
     *
     * @param query Search query string.
     * @param tags Optional comma-separated tag IDs to filter by.
     * @param limit Maximum number of results to return.
     * @return [Result] with a list of matching [Meeting]s on success, or a failure.
     */
    suspend operator fun invoke(
        query: String,
        tags: String? = null,
        limit: Int = 20,
    ): Result<List<Meeting>> =
        meetingsRepository.searchMeetings(query, tags, limit)
}
