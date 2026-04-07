package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import dev.whysoezzy.meetingssdk.models.Interest
import dev.whysoezzy.meetingssdk.models.UpdateInterestsBody

interface InterestsApi{

    @GET("interests")
    suspend fun getAll(): List<Interest>

    @PUT("users/me/interests")
    suspend fun updateMine(@Body body: UpdateInterestsBody): List<Interest>
}