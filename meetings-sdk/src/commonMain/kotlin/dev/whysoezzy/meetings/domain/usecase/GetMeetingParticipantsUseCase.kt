package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository

/**
 * Use case for fetching the list of participants for a specific meeting.
 */
class GetMeetingParticipantsUseCase(
    private val meetingsRepository: MeetingsRepository,
) {
    /**
     * Fetches the participants of a meeting.
     *
     * @param meetingId Unique identifier of the meeting.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of participants to return.
     * @return [Result] with a list of [Person]s on success, or a failure.
     */
    suspend operator fun invoke(
        meetingId: String,
        cursor: String? = null,
        limit: Int = 30,
    ): Result<List<Person>> =
        meetingsRepository.getMeetingParticipants(meetingId, cursor, limit)
}
