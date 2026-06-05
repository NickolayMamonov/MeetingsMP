package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for subscribing the current user to a community.
 */
class SubscribeToCommunityUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Subscribes the current user to the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend operator fun invoke(communityId: Long): Result<Unit> =
        communitiesRepository.subscribeToCommunity(communityId)
}
