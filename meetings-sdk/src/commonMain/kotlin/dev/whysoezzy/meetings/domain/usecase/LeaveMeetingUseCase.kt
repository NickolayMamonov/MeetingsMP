package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for unregistering the current user from a meeting.
 */
class LeaveMeetingUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Unregisters the current user from the specified meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend operator fun invoke(meetingId: String): Result<Unit> =
        meetingsRepository.leaveMeeting(meetingId)
}
