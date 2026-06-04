package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RequestCodeBody
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse
import dev.whysoezzy.meetingssdk.models.VerifyCodeBody

/**
 * Ktorfit API interface for authentication endpoints.
 */
interface AuthApi {

    /**
     * Request a verification code to be sent to the given phone number.
     *
     * @param body Contains the phone number and first name.
     * @return [RequestCodeResponse] with retry timing information.
     */
    @POST("auth/request-code")
    suspend fun requestCode(@Body body: RequestCodeBody): RequestCodeResponse

    /**
     * Verify the code sent to the user's phone and complete authentication.
     *
     * @param body Contains the phone number and the received code.
     * @return [AuthResponse] with the authentication token and user profile.
     */
    @POST("auth/verify-code")
    suspend fun verifyCode(@Body body: VerifyCodeBody): AuthResponse

    /**
     * Log out the current user by invalidating the session on the server.
     */
    @POST("auth/logout")
    suspend fun logout()
}
