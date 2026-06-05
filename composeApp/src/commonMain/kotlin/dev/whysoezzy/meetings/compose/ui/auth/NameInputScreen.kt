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
import dev.whysoezzy.meetings.compose.viewmodel.NameInputViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Name input screen — third step of the auth flow for new users.
 *
 * Allows the user to set their first and last name after phone verification.
 *
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel managing name input state.
 */
@Suppress("LongMethod", "UnusedParameter")
@Composable
fun NameInputScreen(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: NameInputViewModel = koinViewModel(),
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is AuthNavEvent.NavigateToCodeVerification -> {
                    navController.popBackStack()
                }
                is AuthNavEvent.NavigateToNameInput -> {
                    // Already on this screen
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
            text = "Расскажите о себе",
            style = MeetingsTheme.typography.headlineLarge,
            color = MeetingsTheme.colors.onBackground,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        Text(
            text = "Как вас зовут?",
            style = MeetingsTheme.typography.bodyMedium,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        UIKitInput(
            value = uiState.firstName,
            onValueChange = { viewModel.onEvent(NameInputEvent.FirstNameChanged(it)) },
            label = "Имя",
            placeholder = "Введите имя",
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        UIKitInput(
            value = uiState.lastName,
            onValueChange = { viewModel.onEvent(NameInputEvent.LastNameChanged(it)) },
            label = "Фамилия",
            placeholder = "Введите фамилию",
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
            text = "Сохранить",
            onClick = { viewModel.onEvent(NameInputEvent.Save) },
            enabled = uiState.firstName.isNotBlank() && !uiState.isLoading,
            loading = uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
