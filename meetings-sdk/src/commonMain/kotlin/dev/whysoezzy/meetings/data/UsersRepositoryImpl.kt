package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.data.mapper.toDomainCommunities
import dev.whysoezzy.meetings.data.mapper.toDomainMeetings
import dev.whysoezzy.meetings.data.mapper.toDomainTag
import dev.whysoezzy.meetings.data.mapper.toDomainUser
import dev.whysoezzy.meetings.data.mapper.toUpdateUserBody
import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.User
import dev.whysoezzy.meetings.domain.repository.UsersRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.safeApiCall

/**
 * Implementation of [UsersRepository] that delegates to [MeetingsClient]
 * for network operations and maps DTOs to domain models.
 */
class UsersRepositoryImpl(
    private val meetingsClient: MeetingsClient,
) : UsersRepository {

    override suspend fun getCurrentUser(): Result<User> {
        return safeApiCall {
            meetingsClient.users.getMe().toDomainUser()
        }
    }

    override suspend fun getUserById(id: String): Result<User> {
        return safeApiCall {
            meetingsClient.users.getById(id).toDomainUser()
        }
    }

    override suspend fun updateUserProfile(
        user: User,
        interestIds: List<Long>?,
    ): Result<User> {
        return safeApiCall {
            val updateBody = user.toUpdateUserBody()
            meetingsClient.users.updateMe(updateBody).toDomainUser()
        }
    }

    override suspend fun getUserMeetings(userId: String): Result<List<Meeting>> {
        return safeApiCall {
            val profile = meetingsClient.users.getById(userId)
            profile.events?.toDomainMeetings() ?: emptyList()
        }
    }

    override suspend fun getUserCommunities(userId: String): Result<List<Community>> {
        return safeApiCall {
            val profile = meetingsClient.users.getById(userId)
            profile.communities?.toDomainCommunities() ?: emptyList()
        }
    }

    override suspend fun getAllTags(): Result<List<Tag>> {
        return safeApiCall {
            meetingsClient.interests.getAll().map { it.toDomainTag() }
        }
    }
}
