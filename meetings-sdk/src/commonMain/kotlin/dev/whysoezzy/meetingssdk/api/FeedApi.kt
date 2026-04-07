package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.FeedResponse

interface FeedApi{

    @GET("feed")
    suspend fun getFeed(
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 10,
        @Query("tags") tags: String? = null
    ): FeedResponse
}