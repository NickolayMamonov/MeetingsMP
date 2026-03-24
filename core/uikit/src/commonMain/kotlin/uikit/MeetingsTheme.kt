package uikit

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography as M3Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun MeetingsTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    colors: ColorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme(),
    typography: M3Typography = typography(),
    shapes: Shapes = shapes(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorScheme provides colors,
        LocalShapes provides shapes,
    ) {
        MaterialTheme(
            colorScheme = colors.toM3ColorScheme(),
            typography = typography,
            shapes = shapes.toM3Shapes(),
            content = content
        )
    }
}

object MeetingsTheme {

    val colors: ColorScheme
        @Composable @ReadOnlyComposable get() = LocalColorScheme.current

    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = LocalShapes.current

    val typography: M3Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography
}