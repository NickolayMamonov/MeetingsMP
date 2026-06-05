package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for searching communities by query text.
 */
class SearchCommunitiesUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Searches communities matching the given query.
     *
     * @param query Search query string.
     * @return [Result] with a list of matching [Community]s on success, or a failure.
     */
    suspend operator fun invoke(query: String): Result<List<Community>> =
        communitiesRepository.searchCommunities(query)
}
