package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for fetching recommended communities.
 *
 * Returns communities from the RECOMMENDED_COMMUNITIES section of the feed.
 */
class GetRecommendedCommunitiesUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Fetches the list of recommended communities.
     *
     * @return [Result] with a list of [Community]s on success, or a failure.
     */
    suspend operator fun invoke(): Result<List<Community>> =
        communitiesRepository.getRecommendedCommunities()
}
