package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching paged meetings from the feed.
 *
 * Returns the full [MainScreenData] including pagination cursor
 * so the caller can implement cursor-based pagination.
 */
class GetPagedMeetingsUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches a page of meetings with cursor-based pagination.
     *
     * @param cursor Cursor for the next page, `null` for the first page.
     * @param limit Maximum number of feed sections to return.
     * @param tags Optional comma-separated tag IDs to filter by.
     * @return [Result] with [MainScreenData] on success, or a failure.
     */
    suspend operator fun invoke(
        cursor: String? = null,
        limit: Int = 10,
        tags: String? = null,
    ): Result<MainScreenData> =
        meetingsRepository.getMainScreenData(cursor, limit, tags)
}
