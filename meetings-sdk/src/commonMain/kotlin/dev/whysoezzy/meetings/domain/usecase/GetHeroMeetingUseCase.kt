package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching the hero meeting for the main screen banner.
 *
 * Returns the first event from the EVENT_BANNER section of the feed,
 * or `null` if no banner event is available.
 */
class GetHeroMeetingUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches the hero meeting for the main screen banner.
     *
     * @return [Result] with the hero [Meeting] or `null` on success, or a failure.
     */
    suspend operator fun invoke(): Result<Meeting?> =
        meetingsRepository.getMainScreenData().map { it.heroMeeting }
}
