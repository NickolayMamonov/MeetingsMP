package dev.whysoezzy.meetings.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.di.AppGlueModule
import dev.whysoezzy.meetings.compose.di.AppModule
import dev.whysoezzy.meetings.compose.di.AuthFeatureModule
import dev.whysoezzy.meetings.compose.di.AuthModule
import dev.whysoezzy.meetings.compose.di.CommunitiesModule
import dev.whysoezzy.meetings.compose.di.CommunityModule
import dev.whysoezzy.meetings.compose.di.MainFeatureModule
import dev.whysoezzy.meetings.compose.di.MeetingsDataModule
import dev.whysoezzy.meetings.compose.di.ProfileFeatureModule
import dev.whysoezzy.meetings.compose.di.ProfileModule
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
            modules(
                // Data layer (repositories + SDK clients)
                AppModule,
                AuthModule,
                MeetingsDataModule,
                CommunitiesModule,
                ProfileModule,
                // Feature layer (use cases)
                AuthFeatureModule,
                MainFeatureModule,
                CommunityModule,
                ProfileFeatureModule,
                AppGlueModule,
                // Presentation layer (ViewModels)
                meetingsModule,
            )
        }) {
        MeetingsTheme {
            val navController = rememberMeetNavController(startRoute = MeetRoute.MainScreen)

            MeetNavHost(
                navController = navController,
                modifier = modifier,
            )
        }
    }
}
