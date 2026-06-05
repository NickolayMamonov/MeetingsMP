package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.repository.UsersRepository

/**
 * Use case for fetching all available interest tags.
 *
 * @param usersRepository Repository for user profile data.
 */
class GetAllTagsUseCase(
    private val usersRepository: UsersRepository,
) {
    /**
     * Get all interest tags available in the system.
     *
     * @return [Result] with a list of [Tag]s on success, or a failure.
     */
    suspend operator fun invoke(): Result<List<Tag>> {
        return usersRepository.getAllTags()
    }
}
