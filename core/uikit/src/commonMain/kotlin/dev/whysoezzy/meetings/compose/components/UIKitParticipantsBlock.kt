package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitPerson
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A block displaying event participants with their avatars and names.
 *
 * @param participants List of participants to display.
 * @param modifier Modifier applied to the block.
 * @param maxVisible Maximum number of participants to show before collapsing.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UIKitParticipantsBlock(
    participants: List<UIKitPerson>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 8,
) {
    val visibleParticipants = participants.take(maxVisible)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Participants (${participants.size})",
            style = MeetingsTheme.typography.titleMedium,
            color = MeetingsTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = SpacingTokens.medium),
        )

        Spacer(modifier = Modifier.height(SpacingTokens.small))

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpacingTokens.medium),
            horizontalArrangement = Arrangement.spacedBy(SpacingTokens.small),
            verticalArrangement = Arrangement.spacedBy(SpacingTokens.small),
        ) {
            visibleParticipants.forEach { person ->
                UIKitPersonCard(
                    person = person,
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
