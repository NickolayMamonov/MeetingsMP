package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme

/**
 * A composable divider line for separating content sections.
 *
 * @param modifier Modifier applied to the divider.
 * @param thickness The thickness of the divider line.
 * @param color The color of the divider line. Defaults to the theme's outlineVariant.
 */
@Composable
fun UIKitDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = MeetingsTheme.colors.outlineVariant,
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = thickness,
        color = color,
    )
}
