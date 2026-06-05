package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for fetching a community by its unique identifier.
 */
class GetCommunityByIdUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Fetches the community details by ID.
     *
     * @param id Unique identifier of the community.
     * @return [Result] with [Community] on success, or a failure.
     */
    suspend operator fun invoke(id: Long): Result<Community> =
        communitiesRepository.getCommunityById(id)
}
