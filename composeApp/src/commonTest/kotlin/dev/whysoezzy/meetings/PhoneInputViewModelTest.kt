package dev.whysoezzy.meetings

import dev.whysoezzy.meetings.common.error.ErrorType
import dev.whysoezzy.meetings.compose.ui.auth.PhoneInputEvent
import dev.whysoezzy.meetings.compose.viewmodel.PhoneInputViewModel
import dev.whysoezzy.meetings.domain.repository.AuthRepository
import dev.whysoezzy.meetings.domain.usecase.SendOtpUseCase
import dev.whysoezzy.meetingssdk.models.AuthResponse
import dev.whysoezzy.meetingssdk.models.UserProfile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@Suppress("FunctionNaming")
class PhoneInputViewModelTest {

    @Test
    fun `initial state is empty and not loading`() {
        val viewModel = PhoneInputViewModel(createSendOtpUseCase())

        val state = viewModel.uiState.value
        assertEquals("", state.phone)
        assertEquals("", state.firstName)
        assertFalse(state.isLoading)
        assertEquals(null, state.error)
        assertFalse(state.codeSent)
    }

    @Test
    fun `phone changed updates phone in state`() {
        val viewModel = PhoneInputViewModel(createSendOtpUseCase())

        viewModel.onEvent(PhoneInputEvent.PhoneChanged("+79123456789"))

        assertEquals("+79123456789", viewModel.uiState.value.phone)
    }

    @Test
    fun `firstName changed updates firstName in state`() {
        val viewModel = PhoneInputViewModel(createSendOtpUseCase())

        viewModel.onEvent(PhoneInputEvent.FirstNameChanged("Ivan"))

        assertEquals("Ivan", viewModel.uiState.value.firstName)
    }

    @Test
    fun `clear error resets error to null`() {
        val viewModel = PhoneInputViewModel(createSendOtpUseCase())

        viewModel.onEvent(PhoneInputEvent.ClearError)

        assertEquals(null, viewModel.uiState.value.error)
    }

    private fun createSendOtpUseCase(
        result: Result<Unit> = Result.success(Unit)
    ): SendOtpUseCase {
        val repo = object : AuthRepository {
            override suspend fun sendOtp(phone: String): Result<Unit> = result

            override suspend fun verifyOtp(
                phone: String,
                code: String,
                name: String?,
                surname: String?,
            ): Result<AuthResponse> = Result.success(
                AuthResponse(
                    accessToken = "test-access",
                    refreshToken = "test-refresh",
                    user = UserProfile(id = "0", firstName = "Test"),
                    isNewUser = false,
                )
            )

            override suspend fun logout(): Result<Unit> = Result.success(Unit)

            override fun isLoggedIn(): Boolean = true

            override fun isLoggedInFlow(): kotlinx.coroutines.flow.Flow<Boolean> =
                kotlinx.coroutines.flow.flowOf(true)
        }
        return SendOtpUseCase(repo)
    }
}

