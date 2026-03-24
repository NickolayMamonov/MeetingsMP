package uikit

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography as M3Typography


private val DefaultTextStyle = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
)

internal fun typography(): M3Typography = M3Typography(
    displayLarge  = DefaultTextStyle.copy(fontSize = 57.sp, fontWeight = FontWeight.Bold),
    displayMedium = DefaultTextStyle.copy(fontSize = 45.sp, fontWeight = FontWeight.Bold),
    displaySmall  = DefaultTextStyle.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold),

    headlineLarge  = DefaultTextStyle.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    headlineMedium = DefaultTextStyle.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold),
    headlineSmall  = DefaultTextStyle.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold),

    titleLarge  = DefaultTextStyle.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = DefaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    titleSmall  = DefaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold),

    bodyLarge  = DefaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    bodyMedium = DefaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    bodySmall  = DefaultTextStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal),

    labelLarge  = DefaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = DefaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    labelSmall  = DefaultTextStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)