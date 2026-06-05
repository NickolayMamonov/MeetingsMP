package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for fetching a user's profile by their unique identifier.
 *
 * @param usersRepository Repository for user profile data.
 */
class GetUserByIdUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Get a user's profile by their ID.
     *
     * @param userId Unique identifier of the user.
     * @return [Result] with [User] on success, or a failure.
     */
    suspend operator fun invoke(userId: String): Result<User> {
        return usersRepository.getUserById(userId)
    }
}
