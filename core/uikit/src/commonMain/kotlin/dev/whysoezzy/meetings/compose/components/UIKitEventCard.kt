package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.models.UIKitStatus
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A card displaying an event/meeting in a list or grid.
 *
 * @param meeting The meeting info to display.
 * @param onClick Callback invoked when the card is clicked.
 * @param modifier Modifier applied to the card.
 */
@Composable
fun UIKitEventCard(
    meeting: UIKitMeetingInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = MeetingsTheme.shapes.medium
    val statusColor = when (meeting.meetingStatus) {
        UIKitStatus.ACTIVE -> MeetingsTheme.colors.eventOnline
        UIKitStatus.COMPLETED -> MeetingsTheme.colors.eventFinished
        UIKitStatus.CANCELLED -> MeetingsTheme.colors.error
        UIKitStatus.FULL -> MeetingsTheme.colors.eventOffline
        UIKitStatus.DRAFT -> MeetingsTheme.colors.outline
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MeetingsTheme.colors.surface)
            .clickable(onClick = onClick)
            .padding(SpacingTokens.medium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = meeting.title,
                style = MeetingsTheme.typography.titleMedium,
                color = MeetingsTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = meeting.meetingStatus.name,
                style = MeetingsTheme.typography.labelSmall,
                color = statusColor,
            )
        }

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        if (meeting.address.isNotEmpty()) {
            Text(
                text = meeting.address,
                style = MeetingsTheme.typography.bodyMedium,
                color = MeetingsTheme.colors.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
