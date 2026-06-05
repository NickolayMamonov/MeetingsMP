package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for registering the current user for a meeting.
 */
class JoinMeetingUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Registers the current user for the specified meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend operator fun invoke(meetingId: String): Result<Unit> =
        meetingsRepository.joinMeeting(meetingId)
}
