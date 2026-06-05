package dev.whysoezzy.meetings.compose.tokens

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Design tokens for typography used across the Meetings app.
 */
object TypographyTokens {

    private val defaultTextStyle = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
    )

    // ── display ───────────────────────────────────────────────────
    val displayLarge = defaultTextStyle.copy(fontSize = 57.sp, fontWeight = FontWeight.Bold)
    val displayMedium = defaultTextStyle.copy(fontSize = 45.sp, fontWeight = FontWeight.Bold)
    val displaySmall = defaultTextStyle.copy(fontSize = 36.sp, fontWeight = FontWeight.Bold)

    // ── headline ──────────────────────────────────────────────────
    val headlineLarge = defaultTextStyle.copy(fontSize = 32.sp, fontWeight = FontWeight.Bold)
    val headlineMedium = defaultTextStyle.copy(fontSize = 28.sp, fontWeight = FontWeight.Bold)
    val headlineSmall = defaultTextStyle.copy(fontSize = 24.sp, fontWeight = FontWeight.Bold)

    // ── title ─────────────────────────────────────────────────────
    val titleLarge = defaultTextStyle.copy(fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
    val titleMedium = defaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    val titleSmall = defaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

    // ── body ──────────────────────────────────────────────────────
    val bodyLarge = defaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.Normal)
    val bodyMedium = defaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Normal)
    val bodySmall = defaultTextStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Normal)

    // ── label ─────────────────────────────────────────────────────
    val labelLarge = defaultTextStyle.copy(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    val labelMedium = defaultTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)
    val labelSmall = defaultTextStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Medium)
}
