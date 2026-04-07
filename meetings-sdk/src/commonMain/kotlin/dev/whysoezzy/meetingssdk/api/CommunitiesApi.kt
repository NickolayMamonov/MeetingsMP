package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.CommunityFull
import dev.whysoezzy.meetingssdk.models.SubscriptionCount

interface CommunitiesApi{

    @GET("communities/{id}")
    suspend fun getById(
        @Path("id") communityId: String,
        @Query("pastEventsCursor") pastEventsCursor: String? = null,
        @Query("pastEventsLimit") pastEventsLimit: Int = 5
    ): CommunityFull

    @POST("communities/{id}/subscriptions")
    suspend fun subscribe(@Path("id") communityId: String) : SubscriptionCount

    @DELETE("communities/{id}/subscriptions")
    suspend fun unsubscribe(@Path("id") communityId: String)
}