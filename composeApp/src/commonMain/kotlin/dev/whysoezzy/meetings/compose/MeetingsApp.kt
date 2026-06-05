package dev.whysoezzy.meetings.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.di.meetingsModule
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.ui.navigation.MeetNavHost
import dev.whysoezzy.meetings.compose.ui.navigation.MeetRoute
import dev.whysoezzy.meetings.compose.ui.navigation.rememberMeetNavController
import org.koin.compose.KoinApplication

/**
 * Root composable for the Meetings application.
 *
 * Initializes Koin dependency injection and renders the main navigation host.
 *
 * @param modifier Modifier applied to the root layout.
 */
@Composable
fun MeetingsApp(modifier: Modifier = Modifier) {
    KoinApplication(application = {
        modules(meetingsModule)
    }) {
        MeetingsTheme {
            val navController = rememberMeetNavController(startRoute = MeetRoute.Splash)

            MeetNavHost(
                navController = navController,
                modifier = modifier,
            )
        }
    }
}
