package dev.whysoezzy.meetingssdk.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RefreshTokenBody
import dev.whysoezzy.meetingssdk.models.RefreshTokenResponse
import dev.whysoezzy.meetingssdk.models.SendOtpBody
import dev.whysoezzy.meetingssdk.models.VerifyOtpBody

/**
 * Ktorfit API interface for authentication endpoints.
 *
 * Provides methods for phone-based authentication flow:
 * send OTP → verify OTP → refresh token → logout.
 *
 * Endpoints follow the backend contract:
 * - `auth/send-otp` — request a verification code
 * - `auth/verify-otp` — verify the code and authenticate
 * - `auth/refresh` — refresh an expired access token
 * - `auth/logout` — invalidate the session
 */
interface AuthApi {

    /**
     * Send a one-time password to the given phone number.
     *
     * @param body Contains the phone number.
     * @return Unit on success (the backend returns a simple confirmation).
     */
    @POST("auth/send-otp")
    suspend fun sendOtp(@Body body: SendOtpBody)

    /**
     * Verify the OTP code sent to the user's phone and complete authentication.
     *
     * On success, returns both access and refresh tokens along with the user profile.
     *
     * @param body Contains the phone number, code, and optional name/surname for new users.
     * @return [AuthResponse] with access token, refresh token, and user profile.
     */
    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body body: VerifyOtpBody): AuthResponse

    /**
     * Refresh the access token using a valid refresh token.
     *
     * The refresh token is not rotated — the same UUID remains valid until
     * its expiration (30 days) or explicit logout.
     *
     * @param body Contains the refresh token.
     * @return [RefreshTokenResponse] with the new access token.
     */
    @POST("auth/refresh")
    suspend fun refreshToken(@Body body: RefreshTokenBody): RefreshTokenResponse

    /**
     * Log out the current user by invalidating the session on the server.
     * Requires a valid access token in the Authorization header.
     */
    @POST("auth/logout")
    suspend fun logout()
}

