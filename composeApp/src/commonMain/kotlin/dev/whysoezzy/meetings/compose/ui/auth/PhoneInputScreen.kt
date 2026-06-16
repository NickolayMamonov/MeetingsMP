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
import dev.whysoezzy.meetings.compose.viewmodel.PhoneInputViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Phone number input screen — first step of the auth flow.
 *
 * Collects the user's phone number and sends an OTP code for verification.
 * First name is collected in the separate [NameInputScreen] step.
 *
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel managing phone input state.
 */
@Suppress("LongMethod", "UnusedParameter")
@Composable
fun PhoneInputScreen(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: PhoneInputViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is AuthNavEvent.NavigateToCodeVerification -> {
                    navController.navigate(MeetRoute.CodeVerification(phone = uiState.phone))
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

    Column(modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .padding(SpacingTokens.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Вход",
            style = MeetingsTheme.typography.headlineLarge,
            color = MeetingsTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        UIKitInput(
            value = uiState.phone,
            onValueChange = { viewModel.onEvent(PhoneInputEvent.PhoneChanged(it)) },
            label = "Телефон",
            placeholder = "+7 (___) ___-__-__",
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
            text = "Получить код",
            onClick = { viewModel.onEvent(PhoneInputEvent.SendCode) },
            enabled = uiState.phone.isNotBlank() && !uiState.isLoading,
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
