package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitMeetingInfo
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A lazy list of event/meeting cards.
 *
 * @param meetings List of meetings to display.
 * @param onMeetingClick Callback invoked when a meeting card is clicked.
 * @param modifier Modifier applied to the list.
 */
@Composable
fun UIKitMeetingsList(
    meetings: List<UIKitMeetingInfo>,
    onMeetingClick: (UIKitMeetingInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.small),
        contentPadding = PaddingValues(SpacingTokens.medium),
    ) {
        items(meetings, key = { it.id }) { meeting ->
            UIKitEventCard(
                meeting = meeting,
                onClick = { onMeetingClick(meeting) },
            )
        }
    }
}
