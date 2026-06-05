package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.data.mapper.toDomain
import dev.whysoezzy.meetings.data.mapper.toDomainCommunity
import dev.whysoezzy.meetings.data.mapper.toDomainMeetings
import dev.whysoezzy.meetings.data.mapper.toDomainPersons
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.repository.CommunitiesRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.safeApiCall

/**
 * Implementation of [CommunitiesRepository] that delegates to [MeetingsClient]
 * for network operations and maps DTOs to domain models.
 */
class CommunitiesRepositoryImpl(
    private val meetingsClient: MeetingsClient,
) : CommunitiesRepository {

    override suspend fun getRecommendedCommunities(): Result<List<Community>> {
        return safeApiCall {
            val response = meetingsClient.feed.getFeed()
            response.sections
                .filter { it.communities != null }
                .flatMap { it.communities!! }
                .map { it.toDomain() }
        }
    }

    override suspend fun getCommunityById(id: Long): Result<Community> {
        return safeApiCall {
            meetingsClient.communities.getById(id.toString()).toDomainCommunity()
        }
    }

    override suspend fun subscribeToCommunity(id: Long): Result<Unit> {
        return safeApiCall {
            meetingsClient.communities.subscribe(id.toString())
        }
    }

    override suspend fun unsubscribeFromCommunity(id: Long): Result<Unit> {
        return safeApiCall {
            meetingsClient.communities.unsubscribe(id.toString())
        }
    }

    override suspend fun searchCommunities(query: String): Result<List<Community>> {
        return safeApiCall {
            val response = meetingsClient.events.search(query = query)
            response.communities.map { it.toDomain() }
        }
    }

    override suspend fun getCommunityMeetings(id: Long): Result<List<Meeting>> {
        return safeApiCall {
            val communityFull = meetingsClient.communities.getById(id.toString())
            val upcoming = communityFull.upcomingEvents
            val past = communityFull.pastEvents.items
            (upcoming + past).toDomainMeetings()
        }
    }

    override suspend fun getCommunitySubscribers(id: Long): Result<List<Person>> {
        return safeApiCall {
            val communityFull = meetingsClient.communities.getById(id.toString())
            communityFull.subscribersPreview.toDomainPersons()
        }
    }
}
