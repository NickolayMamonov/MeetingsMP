package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for fetching the list of subscribers for a specific community.
 *
 * @param communitiesRepository Repository for community data.
 */
class GetCommunitySubscribersUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Get the list of subscribers for the given community.
     *
     * @param communityId Unique identifier of the community.
     * @return [Result] with a list of [Person]s on success, or a failure.
     */
    suspend operator fun invoke(communityId: Long): Result<List<Person>> {
        return communitiesRepository.getCommunitySubscribers(communityId)
    }
}
