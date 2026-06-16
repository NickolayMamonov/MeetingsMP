package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.models.AuthResponse

/**
 * Use case for verifying a one-time password (OTP) and completing authentication.
 *
 * On success, both access and refresh tokens are automatically persisted by the repository.
 */
class VerifyOtpUseCase(
    private val authRepository: AuthRepository,
) {
    /**
     * Verifies the OTP code sent to the user's phone.
     *
     * @param phone Phone number that received the code.
     * @param code The verification code to validate.
     * @param name Optional first name for new user registration.
     * @param surname Optional surname for new user registration.
     * @return [Result] with [AuthResponse] containing the tokens and user profile,
     *   or a failure with the appropriate error.
     */
    suspend operator fun invoke(
        phone: String,
        code: String,
        name: String? = null,
        surname: String? = null,
    ): Result<AuthResponse> =
        authRepository.verifyOtp(phone, code, name, surname)
}

