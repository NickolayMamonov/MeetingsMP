package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository

/**
 * Use case for fetching meetings associated with a specific community.
 *
 * @param communitiesRepository Repository for community data.
 */
class GetCommunityMeetingsUseCase(
    private val communitiesRepository: CommunitiesRepository,
) {
    /**
     * Get all meetings (upcoming and past) for the given community.
     *
     * @param communityId Unique identifier of the community.
     * @return [Result] with a list of [Meeting]s on success, or a failure.
     */
    suspend operator fun invoke(communityId: Long): Result<List<Meeting>> {
        return communitiesRepository.getCommunityMeetings(communityId)
    }
}
