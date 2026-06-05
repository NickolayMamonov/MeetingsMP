package dev.whysoezzy.meetings.compose.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Shapes as M3Shapes

internal val LocalShapes = staticCompositionLocalOf { shapes() }

internal fun shapes(): Shapes = Shapes(
    none = RoundedCornerShape(0.dp),
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
    full = RoundedCornerShape(percent = 50),
)

@Immutable
data class Shapes(
    val none: CornerBasedShape,
    val extraSmall: CornerBasedShape,
    val small: CornerBasedShape,
    val medium: CornerBasedShape,
    val large: CornerBasedShape,
    val extraLarge: CornerBasedShape,
    val full: CornerBasedShape,
)

internal fun Shapes.toM3Shapes(): M3Shapes = M3Shapes(
    extraSmall = extraSmall,
    small = small,
    medium = medium,
    large = large,
    extraLarge = extraLarge,
)
