package dev.whysoezzy.meetings.compose.ui.navigation

/**
 * Sealed class representing all navigation routes in the application.
 *
 * Each route corresponds to a screen in the app. Routes with parameters
 * include the parameter as a property for type-safe navigation.
 */
sealed interface MeetRoute {

    /**
     * Splash screen shown during app initialization.
     */
    data object Splash : MeetRoute

    // ── Auth flow ──────────────────────────────────────────────

    /**
     * Phone number input screen (first step of auth).
     */
    data object PhoneInput : MeetRoute

    /**
     * OTP code verification screen (second step of auth).
     *
     * @param phone The phone number being verified.
     */
    data class CodeVerification(val phone: String) : MeetRoute

    /**
     * Name input screen (third step of auth, for new users).
     */
    data object NameInput : MeetRoute

    /**
     * Auth success screen shown after successful authentication.
     */
    data object AuthSuccess : MeetRoute

    // ── Meetings flow ──────────────────────────────────────────

    /**
     * Main screen with feed, hero meeting, and communities.
     */
    data object MainScreen : MeetRoute

    /**
     * Meeting details screen.
     *
     * @param meetingId The ID of the meeting to display.
     */
    data class MeetingDetails(val meetingId: String) : MeetRoute

    /**
     * Meeting participants screen.
     *
     * @param meetingId The ID of the meeting whose participants to display.
     */
    data class MeetingParticipants(val meetingId: String) : MeetRoute

    // ── Communities flow ───────────────────────────────────────

    /**
     * Community details screen.
     *
     * @param communityId The ID of the community to display.
     */
    data class CommunityDetails(val communityId: Long) : MeetRoute

    /**
     * Community subscribers screen.
     *
     * @param communityId The ID of the community whose subscribers to display.
     */
    data class CommunitySubscribers(val communityId: Long) : MeetRoute

    // ── Profile flow ───────────────────────────────────────────

    /**
     * Profile details screen for the current user.
     */
    data object ProfileDetails : MeetRoute

    /**
     * Profile edit screen.
     */
    data object ProfileEdit : MeetRoute
}
