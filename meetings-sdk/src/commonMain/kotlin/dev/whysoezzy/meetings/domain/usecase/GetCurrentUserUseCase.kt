package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for fetching the profile of the currently authenticated user.
 *
 * @param usersRepository Repository for user profile data.
 */
class GetCurrentUserUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Get the current user's profile.
     *
     * @return [Result] with [User] on success, or a failure.
     */
    suspend operator fun invoke(): Result<User> {
        return usersRepository.getCurrentUser()
    }
}
