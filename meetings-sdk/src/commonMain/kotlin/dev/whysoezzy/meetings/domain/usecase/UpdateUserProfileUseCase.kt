package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for updating the current user's profile.
 *
 * Only the provided fields will be updated on the server.
 *
 * @param usersRepository Repository for user profile data.
 */
class UpdateUserProfileUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Update the current user's profile.
     *
     * @param user Updated [User] object with new field values.
     * @param interestIds Optional list of interest IDs to set.
     * @return [Result] with the updated [User] on success, or a failure.
     */
    suspend operator fun invoke(
        user: User,
        interestIds: List<Long>? = null,
    ): Result<User> {
        return usersRepository.updateUserProfile(user, interestIds)
    }
}
