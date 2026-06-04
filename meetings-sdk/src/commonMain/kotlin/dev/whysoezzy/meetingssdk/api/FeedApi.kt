package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.FeedResponse

/**
 * Ktorfit API interface for the main feed endpoint.
 */
interface FeedApi{

    /**
     * Fetch the main feed with pagination and optional tag filters.
     *
     * @param cursor Optional cursor for pagination.
     * @param limit Maximum number of feed sections to return (default 10).
     * @param tags Optional comma-separated interest tag IDs to filter by.
     * @return [FeedResponse] containing feed sections.
     */
    @GET("feed")
    suspend fun getFeed(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 10,
        @Query("tags") tags: String? = null
    ): FeedResponse
}