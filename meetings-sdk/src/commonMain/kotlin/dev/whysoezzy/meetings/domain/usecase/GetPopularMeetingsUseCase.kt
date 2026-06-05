package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching popular/nearest meetings.
 *
 * Returns the list of meetings from the NEAREST_EVENTS section of the feed.
 */
class GetPopularMeetingsUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches the popular/nearest meetings.
     *
     * @return [Result] with a list of [Meeting]s on success, or a failure.
     */
    suspend operator fun invoke(): Result<List<Meeting>> =
        meetingsRepository.getMainScreenData().map { it.nearestMeetings }
}
