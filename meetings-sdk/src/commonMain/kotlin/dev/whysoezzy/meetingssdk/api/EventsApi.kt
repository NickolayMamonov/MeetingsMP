package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query
import dev.whysoezzy.meetingssdk.models.EventFull
import dev.whysoezzy.meetingssdk.models.PaginatedResponse
import dev.whysoezzy.meetingssdk.models.Registration
import dev.whysoezzy.meetingssdk.models.SearchResponse
import dev.whysoezzy.meetingssdk.models.UserShort

interface EventsApi{

    @GET("events/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("tags") tags: String? = null,
        @Query("limit") limit: Int = 20
    ): SearchResponse

    @GET("events/{id}")
    suspend fun getById(@Path("id") eventId: String): EventFull

    @GET("events/{id}/attendees")
    suspend fun getAttendees(
        @Path("id") eventId: String,
        @Query("cursor") cursor: String? = null,
        @Query("limit") limit: Int = 30
    ): PaginatedResponse<UserShort>

    @POST("events/{id}/registrations")
    suspend fun register(@Path("id") eventId: String): Registration

    @DELETE("events/{id}/registrations")
    suspend fun unregister(@Path("id") eventId: String)

}
