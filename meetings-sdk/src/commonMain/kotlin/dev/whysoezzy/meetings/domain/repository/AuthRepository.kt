package dev.whysoezzy.meetings.domain.repository

import dev.whysoezzy.meetingssdk.models.AuthResponse
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for authentication operations.
 *
 * Provides a clean domain-level API for the auth flow:
 * send OTP → verify OTP → refresh → logout.
 */
interface AuthRepository {

    /**
     * Send a one-time password to the given phone number.
     *
     * @param phone Phone number to send the OTP to.
     * @return [Result] with Unit on success, or a failure
     *   with the appropriate [dev.whysoezzy.meetingssdk.ApiException].
     */
    suspend fun sendOtp(phone: String): Result<Unit>

    /**
     * Verify the OTP code sent to the user's phone and complete authentication.
     *
     * On success, both access and refresh tokens are persisted automatically.
     *
     * @param phone Phone number that received the code.
     * @param code The verification code to validate.
     * @param name Optional first name for new user registration.
     * @param surname Optional surname for new user registration.
     * @return [Result] with [AuthResponse] containing the tokens and user profile,
     *   or a failure with the appropriate [dev.whysoezzy.meetingssdk.ApiException].
     */
    suspend fun verifyOtp(
        phone: String,
        code: String,
        name: String? = null,
        surname: String? = null,
    ): Result<AuthResponse>

    /**
     * Log out the current user by invalidating the server session
     * and clearing the locally stored tokens.
     *
     * @return [Result] with Unit on success, or a failure.
     */
    suspend fun logout(): Result<Unit>

    /**
     * Check whether a user is currently authenticated (has a stored access token).
     *
     * @return `true` if an access token is stored, `false` otherwise.
     */
    fun isLoggedIn(): Boolean

    /**
     * Observe the authentication state as a [Flow].
     *
     * Emits `true` when the user is authenticated and `false` when logged out.
     * This allows the UI to reactively respond to auth state changes.
     */
    fun isLoggedInFlow(): Flow<Boolean>
}

