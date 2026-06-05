package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for fetching the list of meetings a specific user is registered for.
 *
 * @param usersRepository Repository for user profile data.
 */
class GetUserMeetingsUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Get meetings the specified user is registered for.
     *
     * @param userId Unique identifier of the user.
     * @return [Result] with a list of [Meeting]s on success, or a failure.
     */
    suspend operator fun invoke(userId: String): Result<List<Meeting>> {
        return usersRepository.getUserMeetings(userId)
    }
}
