package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse
import dev.whysoezzy.meetingssdk.safeApiCall

/**
 * Implementation of [AuthRepository] that delegates to [MeetingsClient]
 * for network operations and uses [TokenProvider] for token persistence.
 *
 * All network calls are wrapped in [safeApiCall] to convert exceptions
 * into typed [Result] failures with [dev.whysoezzy.meetingssdk.ApiException].
 */
class AuthRepositoryImpl(
    private val meetingsClient: MeetingsClient,
    private val tokenProvider: TokenProvider,
) : AuthRepository {

    override suspend fun sendOtp(phone: String, firstName: String): Result<RequestCodeResponse> {
        return safeApiCall {
            meetingsClient.requestCode(phone, firstName)
        }
    }

    override suspend fun verifyOtp(phone: String, code: String): Result<AuthResponse> {
        return safeApiCall {
            meetingsClient.verifyCode(phone, code)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return safeApiCall {
            meetingsClient.logout()
        }
    }

    override fun isLoggedIn(): Boolean {
        return tokenProvider.getToken() != null
    }
}
