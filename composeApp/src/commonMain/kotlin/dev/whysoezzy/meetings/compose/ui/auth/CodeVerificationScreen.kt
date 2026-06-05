package dev.whysoezzy.meetings.compose.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.components.UIKitButton
import dev.whysoezzy.meetings.compose.components.UIKitInput
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.viewmodel.CodeVerificationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * OTP code verification screen — second step of the auth flow.
 *
 * Allows the user to enter the verification code sent to their phone.
 *
 * @param phone The phone number being verified.
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel managing code verification state.
 */
@Composable
fun CodeVerificationScreen(
    phone: String,
    navController: MeetNavController,
    viewModel: CodeVerificationViewModel = koinViewModel { parametersOf(phone) },
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is AuthNavEvent.NavigateToCodeVerification -> {
                    // Already on this screen
                }
                is AuthNavEvent.NavigateToNameInput -> {
                    navController.navigate(MeetRoute.NameInput)
                }
                is AuthNavEvent.NavigateToMain -> {
                    navController.navigateAndClear(MeetRoute.MainScreen)
                }
                is AuthNavEvent.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .padding(SpacingTokens.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Подтверждение",
            style = MeetingsTheme.typography.headlineLarge,
            color = MeetingsTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        Text(
            text = "Код отправлен на $phone",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        UIKitInput(
            value = uiState.code,
            onValueChange = { viewModel.onEvent(CodeVerificationEvent.CodeChanged(it)) },
            label = "Код подтверждения",
            placeholder = "0000",
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        if (uiState.error != null) {
            Spacer(modifier = Modifier.height(SpacingTokens.small))
            Text(
                text = uiState.error.toString(),
                style = MeetingsTheme.typography.bodySmall,
                color = MeetingsTheme.colors.error,
            )
        }

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        UIKitButton(
            text = "Подтвердить",
            onClick = { viewModel.onEvent(CodeVerificationEvent.VerifyCode) },
            enabled = uiState.isValid && !uiState.isLoading,
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}