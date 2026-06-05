package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.CommunityFull
import dev.whysoezzy.meetingssdk.models.CommunityShort
import dev.whysoezzy.meetingssdk.models.EventShort
import dev.whysoezzy.meetingssdk.models.PaginatedResponse
import dev.whysoezzy.meetingssdk.models.SubscriptionCount
import dev.whysoezzy.meetingssdk.models.UserShort

/**
 * Ktorfit API interface for community management endpoints.
 *
 * Provides methods for browsing, searching, and managing community subscriptions.
 */
interface CommunitiesApi {

    /**
     * Get a list of recommended communities for the current user.
     *
     * @param limit Maximum number of communities to return (default 10).
     * @return List of [CommunityShort] items.
     */
    @GET("communities/recommended")
    suspend fun getRecommended(
        @Query("limit") limit: Int = 10,
    ): List<CommunityShort>

    /**
     * Get full community details by ID, including events and subscribers.
     *
     * @param communityId Unique identifier of the community.
     * @param pastEventsCursor Optional cursor for paginating past events.
     * @param pastEventsLimit Maximum number of past events to return (default 5).
     * @return [CommunityFull] with full community details.
     */
    @GET("communities/{id}")
    suspend fun getById(
        @Path("id") communityId: String,
        @Query("pastEventsCursor") pastEventsCursor: String? = null,
        @Query("pastEventsLimit") pastEventsLimit: Int = 5,
    ): CommunityFull

    /**
     * Subscribe the current user to the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @return [SubscriptionCount] with the updated subscriber count.
     */
    @POST("communities/{id}/subscriptions")
    suspend fun subscribe(@Path("id") communityId: String): SubscriptionCount

    /**
     * Unsubscribe the current user from the specified community.
     *
     * @param communityId Unique identifier of the community.
     */
    @DELETE("communities/{id}/subscriptions")
    suspend fun unsubscribe(@Path("id") communityId: String)

    /**
     * Search communities by query text.
     *
     * @param query Search query string.
     * @param limit Maximum number of results to return (default 20).
     * @return List of [CommunityShort] matching the query.
     */
    @GET("communities/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("limit") limit: Int = 20,
    ): List<CommunityShort>

    /**
     * Get events organized by the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of events to return (default 20).
     * @return [PaginatedResponse] of [EventShort] items.
     */
    @GET("communities/{id}/events")
    suspend fun getEvents(
        @Path("id") communityId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<EventShort>

    /**
     * Get a paginated list of subscribers for the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of subscribers to return (default 30).
     * @return [PaginatedResponse] of [UserShort] subscribers.
     */
    @GET("communities/{id}/subscribers")
    suspend fun getSubscribers(
        @Path("id") communityId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 30,
    ): PaginatedResponse<UserShort>
}