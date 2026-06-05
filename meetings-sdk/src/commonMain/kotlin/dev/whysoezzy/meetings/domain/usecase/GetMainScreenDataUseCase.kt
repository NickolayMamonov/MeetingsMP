package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching the main screen data.
 *
 * Returns the complete [MainScreenData] containing hero banner,
 * nearest events, recommended communities, tags, and all events.
 */
class GetMainScreenDataUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches the main screen data with optional pagination and tag filters.
     *
     * @param cursor Optional cursor for pagination.
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
