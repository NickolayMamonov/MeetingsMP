package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

class ManageCommunitySubscriptionUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    suspend operator fun invoke(
        communityId: Long,
        isSubscribed: Boolean,
    ): Result<Unit> =
        if (isSubscribed) {
            communitiesRepository.subscribeToCommunity(communityId)
        } else {
            communitiesRepository.unsubscribeFromCommunity(communityId)
        }
}
