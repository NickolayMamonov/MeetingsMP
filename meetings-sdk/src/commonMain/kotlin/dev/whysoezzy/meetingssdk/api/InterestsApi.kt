package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import dev.whysoezzy.meetingssdk.models.Interest
import dev.whysoezzy.meetingssdk.models.UpdateInterestsBody

/**
 * Ktorfit API interface for interest management endpoints.
 */
interface InterestsApi{

    /**
     * Retrieve the full list of available interests/tags.
     *
     * @return List of all [Interest] objects.
     */
    @GET("interests")
    suspend fun getAll(): List<Interest>

    /**
     * Update the current user's selected interests.
     *
     * @param body Contains the list of interest IDs to set.
     * @return Updated list of [Interest] objects for the user.
     */
    @PUT("users/me/interests")
    suspend fun updateMine(@Body body: UpdateInterestsBody): List<Interest>
}