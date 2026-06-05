package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.data.mapper.toDomain
import dev.whysoezzy.meetings.data.mapper.toDomainPersons
import dev.whysoezzy.meetings.data.mapper.toMainScreenData
import dev.whysoezzy.meetings.domain.models.MainScreenData
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Person
import dev.whysoezzy.meetings.domain.repository.MeetingsRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.safeApiCall

/**
 * Implementation of [MeetingsRepository] that delegates to [MeetingsClient]
 * for network operations and maps DTOs to domain models.
 */
class MeetingsRepositoryImpl(
    private val meetingsClient: MeetingsClient,
) : MeetingsRepository {

    override suspend fun getMainScreenData(
        cursor: String?,
        limit: Int,
        tags: String?,
    ): Result<MainScreenData> {
        return safeApiCall {
            val response = meetingsClient.feed.getFeed(
                cursor = cursor,
                limit = limit,
                tags = tags,
            )
            response.sections.toMainScreenData(
                eventsNextCursor = response.eventsNextCursor,
                eventsTotal = response.eventsTotal,
            )
        }
    }

    override suspend fun getMeetingById(id: String): Result<Meeting> {
        return safeApiCall {
            meetingsClient.events.getById(id).toDomain()
        }
    }

    override suspend fun searchMeetings(
        query: String,
        tags: String?,
        limit: Int,
    ): Result<List<Meeting>> {
        return safeApiCall {
            val response = meetingsClient.events.search(
                query = query,
                tags = tags,
                limit = limit,
            )
            response.events.map { it.toDomain() }
        }
    }

    override suspend fun getMeetingParticipants(
        meetingId: String,
        cursor: String?,
        limit: Int,
    ): Result<List<Person>> {
        return safeApiCall {
            val response = meetingsClient.events.getAttendees(
                eventId = meetingId,
                cursor = cursor,
                limit = limit,
            )
            response.items.toDomainPersons()
        }
    }

    override suspend fun joinMeeting(meetingId: String): Result<Unit> {
        return safeApiCall {
            meetingsClient.events.register(meetingId)
        }
    }

    override suspend fun leaveMeeting(meetingId: String): Result<Unit> {
        return safeApiCall {
            meetingsClient.events.unregister(meetingId)
        }
    }
}
