package dev.whysoezzy.meetings.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.whysoezzy.meetings.compose.models.UIKitTagState
import dev.whysoezzy.meetings.compose.theme.MeetingsTheme
import dev.whysoezzy.meetings.compose.tokens.SpacingTokens

/**
 * A composable tag chip that displays a text label with different visual states.
 *
 * @param text The text to display in the tag.
 * @param state The visual state of the tag (ACTIVE, INACTIVE, SELECTED, DISABLED).
 * @param onClick Callback invoked when the tag is clicked.
 * @param modifier Modifier applied to the tag.
 */
@Composable
fun UIKitTag(
    text: String,
    modifier: Modifier = Modifier,
    state: UIKitTagState = UIKitTagState.ACTIVE,
    onClick: (() -> Unit)? = null,
) {
    val colors = MeetingsTheme.colors
    val (backgroundColor, contentColor) = when (state) {
        UIKitTagState.ACTIVE -> colors.tagBackground to colors.tagContent
        UIKitTagState.SELECTED -> colors.primary to colors.onPrimary
        UIKitTagState.INACTIVE -> colors.surfaceVariant to colors.onSurfaceVariant
        UIKitTagState.DISABLED -> colors.surfaceVariant to colors.outline
    }

    val shape = RoundedCornerShape(SpacingTokens.xsmall)

    Text(
        text = text,
        color = contentColor,
        style = MeetingsTheme.typography.labelSmall,
        modifier = modifier
            .clip(shape)
            .background(backgroundColor)
            .then(
                if (onClick != null && state != UIKitTagState.DISABLED) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = SpacingTokens.small, vertical = SpacingTokens.micro)
    )
}

/**
 * A composable tag chip with an outline border style.
 *
 * @param text The text to display in the tag.
 * @param state The visual state of the tag.
 * @param onClick Callback invoked when the tag is clicked.
 * @param modifier Modifier applied to the tag.
 */
@Composable
fun UIKitTagOutlined(
    text: String,
    modifier: Modifier = Modifier,
    state: UIKitTagState = UIKitTagState.ACTIVE,
    onClick: (() -> Unit)? = null,
) {
    val colors = MeetingsTheme.colors
    val (borderColor, contentColor) = when (state) {
        UIKitTagState.ACTIVE -> colors.tagContent to colors.tagContent
        UIKitTagState.SELECTED -> colors.primary to colors.primary
        UIKitTagState.INACTIVE -> colors.outline to colors.onSurfaceVariant
        UIKitTagState.DISABLED -> colors.outlineVariant to colors.outline
    }

    val shape = RoundedCornerShape(SpacingTokens.xsmall)

    Text(
        text = text,
        color = contentColor,
        style = MeetingsTheme.typography.labelSmall,
        modifier = modifier
            .clip(shape)
            .border(width = 1.dp, color = borderColor, shape = shape)
            .then(
                if (onClick != null && state != UIKitTagState.DISABLED) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = SpacingTokens.small, vertical = SpacingTokens.micro)
    )
}
