package dev.whysoezzy.meetings.domain.repository

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person

interface CommunitiesRepository {
    suspend fun getRecommendedCommunities(): Result<List<Community>>

    suspend fun getCommunityById(id: Long): Result<Community>

    suspend fun subscribeToCommunity(id: Long): Result<Unit>

    suspend fun unsubscribeFromCommunity(id: Long): Result<Unit>

    suspend fun searchCommunities(query: String): Result<List<Community>>

    suspend fun getCommunityMeetings(id: Long): Result<List<Meeting>>

    suspend fun getCommunitySubscribers(id: Long): Result<List<Person>>
}
