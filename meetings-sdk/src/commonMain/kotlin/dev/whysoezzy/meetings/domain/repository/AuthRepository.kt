package dev.whysoezzy.meetings.domain.repository

import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse

/**
 * Repository interface for authentication operations.
 *
 * Provides a clean domain-level API for the auth flow:
 * request code → verify code → refresh → logout.
 */
interface AuthRepository {

    /**
     * Request a verification code to be sent to the given phone number.
     *
     * @param phone Phone number to send the code to.
     * @param firstName User's first name for registration.
     * @return [Result] with [RequestCodeResponse] containing retry timing info,
     *   or a failure with the appropriate [dev.whysoezzy.meetingssdk.ApiException].
     */
    suspend fun sendOtp(phone: String, firstName: String): Result<RequestCodeResponse>

    /**
     * Verify the code sent to the user's phone and complete authentication.
     *
     * On success, the auth token is persisted automatically.
     *
     * @param phone Phone number that received the code.
     * @param code The verification code to validate.
     * @return [Result] with [AuthResponse] containing the token and user profile,
     *   or a failure with the appropriate [dev.whysoezzy.meetingssdk.ApiException].
     */
    suspend fun verifyOtp(phone: String, code: String): Result<AuthResponse>

    /**
     * Log out the current user by invalidating the server session
     * and clearing the locally stored auth token.
     *
     * @return [Result] with Unit on success, or a failure.
     */
    suspend fun logout(): Result<Unit>

    /**
     * Check whether a user is currently authenticated (has a stored token).
     *
     * @return `true` if a token is stored, `false` otherwise.
     */
    fun isLoggedIn(): Boolean

    /**
     * Observe the authentication state as a [kotlinx.coroutines.flow.Flow].
     *
     * Emits `true` when the user is authenticated and `false` when logged out.
     * This allows the UI to reactively respond to auth state changes.
     */
    fun isLoggedInFlow(): kotlinx.coroutines.flow.Flow<Boolean>
}
