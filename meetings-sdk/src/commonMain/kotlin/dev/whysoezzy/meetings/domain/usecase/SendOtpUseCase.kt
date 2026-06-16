package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.AuthRepository

/**
 * Use case for sending a one-time password (OTP) to a phone number.
 *
 * Delegates to [AuthRepository.sendOtp].
 */
class SendOtpUseCase(
    private val authRepository: AuthRepository,
) {
    /**
     * Sends a verification code to the given phone number.
     *
     * @param phone Phone number to send the code to.
     * @return [Result] with Unit on success, or a failure with the appropriate error.
     */
    suspend operator fun invoke(phone: String): Result<Unit> =
        authRepository.sendOtp(phone)
}

