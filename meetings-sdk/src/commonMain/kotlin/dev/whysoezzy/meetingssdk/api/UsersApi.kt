package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import dev.whysoezzy.meetingssdk.models.DeviceTokenBody
import dev.whysoezzy.meetingssdk.models.UpdateUserBody
import dev.whysoezzy.meetingssdk.models.UserProfile

/**
 * Ktorfit API interface for user profile and account management endpoints.
 */
interface UsersApi{
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
}