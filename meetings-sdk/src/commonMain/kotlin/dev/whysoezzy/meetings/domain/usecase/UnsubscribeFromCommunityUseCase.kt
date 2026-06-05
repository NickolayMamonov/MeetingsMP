package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for unsubscribing the current user from a community.
 */
class UnsubscribeFromCommunityUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Unsubscribes the current user from the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @return [Result] with Unit on success, or a failure.
     */
    suspend operator fun invoke(communityId: Long): Result<Unit> =
        communitiesRepository.unsubscribeFromCommunity(communityId)
}
