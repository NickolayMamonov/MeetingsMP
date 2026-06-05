package dev.whysoezzy.meetings.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Window size class classification for adaptive layouts.
 *
 * Compact (< 600dp) — phone portrait
 * Medium (600dp–840dp) — phone landscape / small tablet
 * Expanded (> 840dp) — tablet / desktop
 */
enum class WindowSizeClass {
    COMPACT,
    MEDIUM,
    EXPANDED,
}

/**
 * Holder for current window width and height size classes.
 */
@Immutable
data class WindowSize(
    val width: WindowSizeClass,
    val height: WindowSizeClass,
) {
    companion object {
        val compact = WindowSize(WindowSizeClass.COMPACT, WindowSizeClass.COMPACT)
    }
}

internal val LocalWindowSize = staticCompositionLocalOf { WindowSize.compact }

/**
 * Returns the current [WindowSize] based on the available width and height.
 *
 * @param widthDp Available width in dp.
 * @param heightDp Available height in dp.
 */
@Composable
fun calculateWindowSizeClass(
    widthDp: Dp,
    heightDp: Dp,
): WindowSize {
    val widthClass = when {
        widthDp < 600.dp -> WindowSizeClass.COMPACT
        widthDp < 840.dp -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
    val heightClass = when {
        heightDp < 480.dp -> WindowSizeClass.COMPACT
        heightDp < 900.dp -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
    return WindowSize(widthClass, heightClass)
}
