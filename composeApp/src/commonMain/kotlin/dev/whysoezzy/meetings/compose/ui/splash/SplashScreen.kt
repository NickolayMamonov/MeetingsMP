package dev.whysoezzy.meetings.compose.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.viewmodel.AuthCheckViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Splash screen shown during app initialization.
 *
 * Checks authentication state and navigates to the appropriate
 * destination (auth flow or main screen).
 *
 * @param navController Navigation controller for routing.
 * @param viewModel ViewModel for checking auth state.
 */
@Suppress("UnusedParameter")
@Composable
fun SplashScreen(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
    viewModel: AuthCheckViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.navEvent.collect { event ->
            when (event) {
                is dev.whysoezzy.meetings.compose.ui.navigation.AuthCheckNavEvent.NavigateToAuth -> {
                    navController.navigateAndClear(MeetRoute.PhoneInput)
                }
                is dev.whysoezzy.meetings.compose.ui.navigation.AuthCheckNavEvent.NavigateToMain -> {
                    navController.navigateAndClear(MeetRoute.MainScreen)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.checkAuth()
    }

    Column(modifier = modifier, 
        modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Meetings",
            style = MeetingsTheme.typography.headlineLarge,
            color = MeetingsTheme.colors.primary,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.large))

        CircularProgressIndicator(
            color = MeetingsTheme.colors.primary,
            modifier = Modifier.size(32.dp),
        )
    }
}
