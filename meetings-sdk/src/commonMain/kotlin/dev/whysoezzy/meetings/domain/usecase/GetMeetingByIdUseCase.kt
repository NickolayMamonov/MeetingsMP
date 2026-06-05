package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching a single meeting by its unique identifier.
 */
class GetMeetingByIdUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches the meeting details by ID.
     *
     * @param id Unique identifier of the meeting.
     * @return [Result] with [Meeting] on success, or a failure.
     */
    suspend operator fun invoke(id: String): Result<Meeting> =
        meetingsRepository.getMeetingById(id)
}
