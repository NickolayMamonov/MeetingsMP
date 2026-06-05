package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for fetching the list of communities a specific user is subscribed to.
 *
 * @param usersRepository Repository for user profile data.
 */
class GetUserCommunitiesUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Get communities the specified user is subscribed to.
     *
     * @param userId Unique identifier of the user.
     * @return [Result] with a list of [Community]s on success, or a failure.
     */
    suspend operator fun invoke(userId: String): Result<List<Community>> {
        return usersRepository.getUserCommunities(userId)
    }
}
