package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RequestCodeBody
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse
import dev.whysoezzy.meetingssdk.models.VerifyCodeBody

interface AuthApi {

    @POST("auth/request-code")
    suspend fun requestCode(@Body body: RequestCodeBody): RequestCodeResponse

    @POST("auth/verify-code")
    suspend fun verifyCode(@Body body: VerifyCodeBody): AuthResponse

    @POST("auth/logout")
    suspend fun logout()
}
