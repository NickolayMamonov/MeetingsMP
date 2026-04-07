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



interface UsersApi{
    @GET("users/me")
    suspend fun getMe(): UserProfile

    @GET("users/{id}")
    suspend fun getById(@Path("id") userId: String): UserProfile

    @PATCH("users/me")
    suspend fun updateMe(@Body body: UpdateUserBody): UserProfile

    @DELETE("users/me")
    suspend fun deleteMe()

    @PUT("users/me/device-token")
    suspend fun registerDeviceToken(@Body body: DeviceTokenBody)
}