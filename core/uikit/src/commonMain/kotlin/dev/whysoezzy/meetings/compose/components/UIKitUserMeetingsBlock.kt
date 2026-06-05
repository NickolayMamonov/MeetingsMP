package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A block displaying a user's meetings list with a section header.
 *
 * @param title Section title (e.g., "My Meetings").
 * @param meetings List of meetings to display.
 * @param onMeetingClick Callback invoked when a meeting is clicked.
 * @param modifier Modifier applied to the block.
 */
@Composable
fun UIKitUserMeetingsBlock(
    title: String,
    meetings: List<UIKitMeetingInfo>,
    onMeetingClick: (UIKitMeetingInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MeetingsTheme.typography.titleLarge,
            color = MeetingsTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = SpacingTokens.medium),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        UIKitMeetingsList(
            meetings = meetings,
            onMeetingClick = onMeetingClick,
            modifier = Modifier.padding(top = SpacingTokens.micro),
        )
    }
}
