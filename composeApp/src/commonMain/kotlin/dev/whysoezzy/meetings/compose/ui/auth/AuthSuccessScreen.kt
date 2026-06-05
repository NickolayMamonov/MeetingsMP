package dev.whysoezzy.meetings.compose.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavController
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute

/**
 * Auth success screen shown after successful authentication.
 *
 * Displays a confirmation message and automatically navigates
 * to the main screen.
 *
 * @param navController Navigation controller for routing.
 */
@Suppress("UnusedParameter")
@Composable
fun AuthSuccessScreen(
    navController: MeetNavController,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        navController.navigateAndClear(MeetRoute.MainScreen)
    }

    Column(modifier = Modifier
            .fillMaxSize()
            .background(MeetingsTheme.colors.background)
            .padding(SpacingTokens.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Добро пожаловать!",
            style = MeetingsTheme.typography.headlineLarge,
            color = MeetingsTheme.colors.primary,
        )

        Spacer(modifier = Modifier.height(SpacingTokens.medium))

        Text(
            text = "Вы успешно вошли в приложение",
            style = MeetingsTheme.typography.bodyLarge,
            color = MeetingsTheme.colors.onSurfaceVariant,
        )
    }
}
