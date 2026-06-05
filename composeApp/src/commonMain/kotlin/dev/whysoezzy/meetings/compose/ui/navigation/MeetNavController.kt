package dev.whysoezzy.meetings.compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

/**
 * Simple navigation controller that manages a back stack of [MeetRoute]s.
 *
 * Provides push (navigate), pop (navigate back), and replace (navigate and clear)
 * operations. The current route is exposed as a state that Compose can observe.
 */
@Stable
class MeetNavController(private val startRoute: MeetRoute) {

    private val backStack: MutableList<MeetRoute> = mutableListOf(startRoute)

    /**
     * The current route displayed on screen.
     */
    var currentRoute: MeetRoute = startRoute
        private set

    /**
     * Navigate to a new route, pushing the current route onto the back stack.
     */
    fun navigate(route: MeetRoute) {
        backStack.add(currentRoute)
        currentRoute = route
    }

    /**
     * Navigate to a new route, replacing the current route (no back stack entry).
     */
    fun replace(route: MeetRoute) {
        currentRoute = route
    }

    /**
     * Navigate back to the previous route in the back stack.
     *
     * @return `true` if navigation was successful, `false` if the back stack is empty.
     */
    fun popBackStack(): Boolean {
        if (backStack.isEmpty()) return false
        currentRoute = backStack.removeAt(backStack.lastIndex)
        return true
    }

    /**
     * Clear the entire back stack and navigate to the given route.
     */
    fun navigateAndClear(route: MeetRoute) {
        backStack.clear()
        currentRoute = route
    }

    companion object {
        /**
         * Saver for persisting and restoring [MeetNavController] state across
         * configuration changes or process death.
         */
        val Saver: Saver<MeetNavController, *> = listSaver(
            save = { navController ->
                listOf(
                    navController.startRoute.toSavedString(),
                    navController.currentRoute.toSavedString(),
                ) + navController.backStack.map { it.toSavedString() }
            },
            restore = { saved ->
                val startRoute = saved[0].toRoute()
                val currentRoute = saved[1].toRoute()
                MeetNavController(startRoute).apply {
                    this.currentRoute = currentRoute
                    this.backStack.clear()
                    this.backStack.addAll(saved.drop(2).map { it.toRoute() })
                }
            },
        )

        private fun MeetRoute.toSavedString(): String = when (this) {
            is MeetRoute.Splash -> "Splash"
            is MeetRoute.PhoneInput -> "PhoneInput"
            is MeetRoute.CodeVerification -> "CodeVerification:$phone"
            is MeetRoute.NameInput -> "NameInput"
            is MeetRoute.AuthSuccess -> "AuthSuccess"
            is MeetRoute.MainScreen -> "MainScreen"
            is MeetRoute.MeetingDetails -> "MeetingDetails:$meetingId"
            is MeetRoute.MeetingParticipants -> "MeetingParticipants:$meetingId"
            is MeetRoute.CommunityDetails -> "CommunityDetails:$communityId"
            is MeetRoute.CommunitySubscribers -> "CommunitySubscribers:$communityId"
            is MeetRoute.ProfileDetails -> "ProfileDetails"
            is MeetRoute.ProfileEdit -> "ProfileEdit"
        }

        private fun String.toRoute(): MeetRoute = when {
            this == "Splash" -> MeetRoute.Splash
            this == "PhoneInput" -> MeetRoute.PhoneInput
            this.startsWith("CodeVerification:") -> MeetRoute.CodeVerification(
                phone = substringAfter("CodeVerification:"),
            )
            this == "NameInput" -> MeetRoute.NameInput
            this == "AuthSuccess" -> MeetRoute.AuthSuccess
            this == "MainScreen" -> MeetRoute.MainScreen
            this.startsWith("MeetingDetails:") -> MeetRoute.MeetingDetails(
                meetingId = substringAfter("MeetingDetails:"),
            )
            this.startsWith("MeetingParticipants:") -> MeetRoute.MeetingParticipants(
                meetingId = substringAfter("MeetingParticipants:"),
            )
            this.startsWith("CommunityDetails:") -> MeetRoute.CommunityDetails(
                communityId = substringAfter("CommunityDetails:").toLong(),
            )
            this.startsWith("CommunitySubscribers:") -> MeetRoute.CommunitySubscribers(
                communityId = substringAfter("CommunitySubscribers:").toLong(),
            )
            this == "ProfileDetails" -> MeetRoute.ProfileDetails
            this == "ProfileEdit" -> MeetRoute.ProfileEdit
            else -> MeetRoute.Splash
        }
    }
}

/**
 * Creates and remembers a [MeetNavController] starting from [startRoute].
 */
@Composable
fun rememberMeetNavController(startRoute: MeetRoute = MeetRoute.Splash): MeetNavController {
    return rememberSaveable(saver = MeetNavController.Saver) {
        MeetNavController(startRoute)
    }
}
