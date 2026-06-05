package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.AdBlock
import dev.whysoezzy.meetingssdk.models.EventFull
import dev.whysoezzy.meetingssdk.models.EventShort
import dev.whysoezzy.meetingssdk.models.PaginatedResponse
import dev.whysoezzy.meetingssdk.models.Registration
import dev.whysoezzy.meetingssdk.models.SearchResponse
import dev.whysoezzy.meetingssdk.models.UserShort

/**
 * Ktorfit API interface for event-related endpoints.
 *
 * Provides methods for browsing, searching, and managing event registrations.
 */
interface EventsApi {

    /**
     * Search events and communities by query text and optional tag filters.
     *
     * @param query Search query string.
     * @param tags Optional comma-separated interest tag IDs to filter by.
     * @param limit Maximum number of results to return (default 20).
     * @return [SearchResponse] containing matching events and communities.
     */
    @GET("events/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("tags") tags: String? = null,
        @Query("limit") limit: Int = 20,
    ): SearchResponse

    /**
     * Get full event details by ID.
     *
     * @param eventId Unique identifier of the event.
     * @return [EventFull] with full event details.
     */
    @GET("events/{id}")
    suspend fun getById(@Path("id") eventId: String): EventFull

    /**
     * Get a paginated list of attendees for the specified event.
     *
     * @param eventId Unique identifier of the event.
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of attendees to return (default 30).
     * @return [PaginatedResponse] of [UserShort] attendees.
     */
    @GET("events/{id}/attendees")
    suspend fun getAttendees(
        @Path("id") eventId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 30,
    ): PaginatedResponse<UserShort>

    /**
     * Register the current user for the specified event.
     *
     * @param eventId Unique identifier of the event.
     * @return [Registration] details.
     */
    @POST("events/{id}/registrations")
    suspend fun register(@Path("id") eventId: String): Registration

    /**
     * Unregister the current user from the specified event.
     *
     * @param eventId Unique identifier of the event.
     */
    @DELETE("events/{id}/registrations")
    suspend fun unregister(@Path("id") eventId: String)

    /**
     * Get a list of popular/trending events.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of events to return (default 20).
     * @return [PaginatedResponse] of [EventShort] items.
     */
    @GET("events/popular")
    suspend fun getPopular(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<EventShort>

    /**
     * Get all events with pagination.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of events to return (default 20).
     * @return [PaginatedResponse] of [EventShort] items.
     */
    @GET("events")
    suspend fun getAll(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<EventShort>

    /**
     * Get events the current user has registered for.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of events to return (default 20).
     * @return [PaginatedResponse] of [EventShort] items.
     */
    @GET("users/me/events")
    suspend fun getUserEvents(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 20,
    ): PaginatedResponse<EventShort>

    /**
     * Get ad blocks configured for the events section.
     *
     * @return List of [AdBlock] items.
     */
    @GET("events/ads")
    suspend fun getAds(): List<AdBlock>
}
