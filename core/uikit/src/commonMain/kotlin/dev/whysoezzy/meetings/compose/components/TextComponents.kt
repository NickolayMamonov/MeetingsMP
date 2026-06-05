package dev.whysoezzy.meetings.compose.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme

/**
 * Heading text component following the Meetings design system.
 * Uses headlineLarge typography style.
 *
 * @param text The text content to display.
 * @param modifier Modifier applied to the text.
 * @param textAlign Optional text alignment. Defaults to Start.
 * @param maxLines Maximum number of lines. Defaults to unlimited.
 */
@Composable
fun TextHeading(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MeetingsTheme.typography.headlineLarge,
        color = MeetingsTheme.colors.onSurface,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Subheading text component following the Meetings design system.
 * Uses headlineSmall typography style.
 *
 * @param text The text content to display.
 * @param modifier Modifier applied to the text.
 * @param textAlign Optional text alignment.
 * @param maxLines Maximum number of lines.
 */
@Composable
fun TextSubheading(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MeetingsTheme.typography.headlineSmall,
        color = MeetingsTheme.colors.onSurface,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Body text component following the Meetings design system.
 * Uses bodyLarge typography style.
 *
 * @param text The text content to display.
 * @param modifier Modifier applied to the text.
 * @param textAlign Optional text alignment.
 * @param maxLines Maximum number of lines.
 */
@Composable
fun TextBody(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MeetingsTheme.typography.bodyLarge,
        color = MeetingsTheme.colors.onSurfaceVariant,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Metadata/caption text component following the Meetings design system.
 * Uses bodySmall typography style with onSurfaceVariant color.
 *
 * @param text The text content to display.
 * @param modifier Modifier applied to the text.
 * @param textAlign Optional text alignment.
 * @param maxLines Maximum number of lines.
 */
@Composable
fun TextMetadata(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
) {
    Text(
        text = text,
        modifier = modifier,
        style = MeetingsTheme.typography.bodySmall,
        color = MeetingsTheme.colors.onSurfaceVariant,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis,
    )
}
