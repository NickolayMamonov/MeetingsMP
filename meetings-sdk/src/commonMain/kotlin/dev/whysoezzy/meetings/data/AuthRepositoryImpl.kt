package dev.whysoezzy.meetings.data

import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.MeetingsClient
import dev.whysoezzy.meetingssdk.auth.TokenProvider
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse
import dev.whysoezzy.meetingssdk.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    private val _isLoggedInFlow = MutableStateFlow(tokenProvider.getToken() != null)

    override suspend fun sendOtp(phone: String, firstName: String): Result<RequestCodeResponse> {
        return safeApiCall {
            meetingsClient.requestCode(phone, firstName)
        }
    }

    override suspend fun verifyOtp(phone: String, code: String): Result<AuthResponse> {
        return safeApiCall {
            meetingsClient.verifyCode(phone, code)
        }.also { updateAuthState() }
    }

    override suspend fun logout(): Result<Unit> {
        return safeApiCall {
            meetingsClient.logout()
        }.also { updateAuthState() }
    }

    override fun isLoggedIn(): Boolean {
        return tokenProvider.getToken() != null
    }

    override fun isLoggedInFlow(): Flow<Boolean> = _isLoggedInFlow.asStateFlow()

    private fun updateAuthState() {
        _isLoggedInFlow.value = tokenProvider.getToken() != null
    }
}
