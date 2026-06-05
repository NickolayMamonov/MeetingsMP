package dev.whysoezzy.meetings.domain.repository

import dev.whysoezzy.meetings.domain.models.Community
import dev.whysoezzy.meetings.domain.models.Meeting
import dev.whysoezzy.meetings.domain.models.Tag
import dev.whysoezzy.meetings.domain.models.User

/**
 * Repository interface for user profile operations.
 *
 * Provides a clean domain-level API for fetching and updating
 * user profiles, and retrieving user-related data.
 */
interface UsersRepository {

    /**
     * Get the profile of the currently authenticated user.
     *
     * @return [Result] with [User] on success, or a failure.
     */
    suspend fun getCurrentUser(): Result<User>

    /**
     * Get a user's profile by their unique identifier.
     *
     * @param id Unique identifier of the user.
     * @return [Result] with [User] on success, or a failure.
     */
    suspend fun getUserById(id: String): Result<User>

    /**
     * Update the current user's profile fields.
     * Only the provided fields will be updated.
     *
     * @param user Updated [User] object with new field values.
     * @param interestIds Optional list of interest IDs to set.
     * @return [Result] with the updated [User] on success, or a failure.
     */
    suspend fun updateUserProfile(
        user: User,
        interestIds: List<Long>? = null,
    ): Result<User>

    /**
     * Get the list of meetings the specified user is registered for.
     *
     * @param userId Unique identifier of the user.
     * @return [Result] with a list of [Meeting]s on success, or a failure.
     */
    suspend fun getUserMeetings(userId: String): Result<List<Meeting>>

    /**
     * Get the list of communities the specified user is subscribed to.
     *
     * @param userId Unique identifier of the user.
     * @return [Result] with a list of [Community]s on success, or a failure.
     */
    suspend fun getUserCommunities(userId: String): Result<List<Community>>

    /**
     * Get all available interest tags.
     *
     * @return [Result] with a list of [Tag]s on success, or a failure.
     */
    suspend fun getAllTags(): Result<List<Tag>>
}
