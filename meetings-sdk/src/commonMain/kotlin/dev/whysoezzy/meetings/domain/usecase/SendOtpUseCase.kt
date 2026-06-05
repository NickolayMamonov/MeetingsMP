package dev.whysoezzy.meetings.domain.usecase

import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetingssdk.models.RequestCodeResponse

/**
 * Use case for sending a one-time password (OTP) to a phone number.
 *
 * Delegates to [AuthRepository.sendOtp] and returns the result
 * containing retry timing information.
 */
class SendOtpUseCase(
    private val authRepository: AuthRepository,
) {
    /**
     * Sends a verification code to the given phone number.
     *
     * @param phone Phone number to send the code to.
     * @param firstName User's first name for registration.
     * @return [Result] with [RequestCodeResponse] on success,
     *   or a failure with the appropriate error.
     */
    suspend operator fun invoke(phone: String, firstName: String): Result<RequestCodeResponse> =
        authRepository.sendOtp(phone, firstName)
}
