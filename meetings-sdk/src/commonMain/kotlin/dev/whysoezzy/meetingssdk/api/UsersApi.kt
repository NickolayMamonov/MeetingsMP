package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.CommunityShort
import dev.whysoezzy.meetingssdk.models.DeviceTokenBody
import dev.whysoezzy.meetingssdk.models.EventShort
import dev.whysoezzy.meetingssdk.models.PaginatedResponse
import dev.whysoezzy.meetingssdk.models.UpdateUserBody
import dev.whysoezzy.meetingssdk.models.UserProfile

/**
 * Ktorfit API interface for user profile and account management endpoints.
 *
 * Provides methods for viewing and updating user profiles,
 * managing device tokens, and fetching user-associated events and communities.
 */
interface UsersApi {

    /**
     * Get the profile of the currently authenticated user.
     *
     * @return [UserProfile] of the current user.
     */
    @GET("users/me")
    suspend fun getMe(): UserProfile

    /**
     * Get a user's profile by their unique ID.
     *
     * @param userId Unique identifier of the user.
     * @return [UserProfile] of the requested user.
     */
    @GET("users/{id}")
    suspend fun getById(@Path("id") userId: String): UserProfile

    /**
     * Update the current user's profile fields.
     * Only provided fields will be updated.
     *
     * @param body Contains the fields to update.
     * @return Updated [UserProfile].
     */
    @PATCH("users/me")
    suspend fun updateMe(@Body body: UpdateUserBody): UserProfile

    /**
     * Delete the current user's account.
     */
    @DELETE("users/me")
    suspend fun deleteMe()

    /**
     * Register a device token for push notifications.
     *
     * @param body Contains the device token and platform.
     */
    @PUT("users/me/device-token")
    suspend fun registerDeviceToken(@Body body: DeviceTokenBody)

    /**
     * Get events the specified user is registered for.
     *
     * @param userId Unique identifier of the user.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of events to return (default 20).
     * @return [PaginatedResponse] of [EventShort] items.
     */
    @GET("users/{id}/events")
    suspend fun getUserEvents(
        @Path("id") userId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<EventShort>

    /**
     * Get communities the specified user is subscribed to.
     *
     * @param userId Unique identifier of the user.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of communities to return (default 20).
     * @return [PaginatedResponse] of [CommunityShort] items.
     */
    @GET("users/{id}/communities")
    suspend fun getUserCommunities(
        @Path("id") userId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<CommunityShort>
}