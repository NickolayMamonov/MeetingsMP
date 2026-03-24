package uikit

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Shapes as M3Shapes

internal val LocalShapes = staticCompositionLocalOf { shapes() }

internal fun shapes(): Shapes = Shapes(
    none = RoundedCornerShape(0.dp),
    extraSmall = RoundedCornerShape(6.dp),   // чипы/теги
    small = RoundedCornerShape(8.dp),        // инпуты
    medium = RoundedCornerShape(12.dp),      // карточки
    large = RoundedCornerShape(16.dp),       // bottom sheets
    extraLarge = RoundedCornerShape(28.dp),  // большие карточки
    full = RoundedCornerShape(percent = 50)  // кнопки pill-shape
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