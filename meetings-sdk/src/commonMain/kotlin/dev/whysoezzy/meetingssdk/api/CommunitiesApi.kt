package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.CommunityFull
import dev.whysoezzy.meetingssdk.models.SubscriptionCount

/**
 * Ktorfit API interface for community management endpoints.
 */
interface CommunitiesApi{

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
        @Query("pastEventsLimit") pastEventsLimit: Int = 5
    ): CommunityFull

    /**
     * Subscribe the current user to the specified community.
     *
     * @param communityId Unique identifier of the community.
     * @return [SubscriptionCount] with the updated subscriber count.
     */
    @POST("communities/{id}/subscriptions")
    suspend fun subscribe(@Path("id") communityId: String) : SubscriptionCount

    /**
     * Unsubscribe the current user from the specified community.
     *
     * @param communityId Unique identifier of the community.
     */
    @DELETE("communities/{id}/subscriptions")
    suspend fun unsubscribe(@Path("id") communityId: String)
}