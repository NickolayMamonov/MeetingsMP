package dev.whysoezzy.meetings.compose.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A toggle switch component for boolean settings.
 *
 * @param checked Whether the toggle is in the "on" state.
 * @param onCheckedChange Callback invoked when the toggle state changes.
 * @param modifier Modifier applied to the toggle.
 * @param enabled Whether the toggle is interactive.
 */
@Composable
fun UIKitToggle(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = MeetingsTheme.colors
    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.primary else colors.surfaceVariant,
        animationSpec = tween(durationMillis = 200),
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) colors.onPrimary else colors.outline,
        animationSpec = tween(durationMillis = 200),
    )

    val trackWidth = 48.dp
    val trackHeight = 28.dp
    val thumbSize = 20.dp

    val isClickable = enabled && onCheckedChange != null
    var resultModifier = modifier
        .width(trackWidth)
        .size(trackWidth, trackHeight)
        .clip(MeetingsTheme.shapes.full)
        .background(trackColor)
    if (isClickable) {
        resultModifier = resultModifier.clickable { onCheckedChange!!(!checked) }
    }

    Row(
        modifier = resultModifier,
        horizontalArrangement = if (checked) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(thumbSize)
                .clip(MeetingsTheme.shapes.full)
                .background(thumbColor),
        )
    }
}
