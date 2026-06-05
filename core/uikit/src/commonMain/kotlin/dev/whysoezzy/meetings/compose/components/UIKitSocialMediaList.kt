package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.whysoezzy.meetings.compose.models.UIKitSocialMedia
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * Displays a list of social media links.
 *
 * @param socialMedias List of social media entries to display.
 * @param modifier Modifier applied to the component.
 */
@Composable
fun UIKitSocialMediaList(
    socialMedias: List<UIKitSocialMedia>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpacingTokens.small),
    ) {
        socialMedias.forEach { socialMedia ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = SpacingTokens.micro),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = socialMedia.type.name,
                    style = MeetingsTheme.typography.bodyMedium,
                    color = MeetingsTheme.colors.onSurface,
                )
                Text(
                    text = socialMedia.username,
                    style = MeetingsTheme.typography.bodyMedium,
                    color = MeetingsTheme.colors.onSurfaceVariant,
                )
            }
        }
    }
}
