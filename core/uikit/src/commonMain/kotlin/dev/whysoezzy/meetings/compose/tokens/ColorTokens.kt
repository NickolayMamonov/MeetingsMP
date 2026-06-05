@file:Suppress("MagicNumber")

package dev.whysoezzy.meetings.compose.tokens

import androidx.compose.ui.graphics.Color

/**
 * Design tokens for colors used across the Meetings app.
 *
 * These tokens provide semantic color mappings on top of the core
 * [dev.whysoezzy.meetings.compose.models.UIKitColorScheme] values.
 */
object ColorTokens {

    // ── primary palette ────────────────────────────────────────
    val primary = Color(0xFF9000D4)
    val onPrimary = Color(0xFFFFFFFF)
    val primaryContainer = Color(0xFFF3D9FF)
    val onPrimaryContainer = Color(0xFF21003D)

    // ── secondary palette ──────────────────────────────────────
    val secondary = Color(0xFF5C5C5C)
    val onSecondary = Color(0xFFFFFFFF)
    val secondaryContainer = Color(0xFFE8DEF8)
    val onSecondaryContainer = Color(0xFF1A1A1A)

    // ── tertiary palette ───────────────────────────────────────
    val tertiary = Color(0xFF6D009F)
    val onTertiary = Color(0xFFFFFFFF)
    val tertiaryContainer = Color(0xFFFAEFFF)
    val onTertiaryContainer = Color(0xFF21003D)

    // ── error palette ──────────────────────────────────────────
    val error = Color(0xFFBA1A1A)
    val onError = Color(0xFFFFFFFF)
    val errorContainer = Color(0xFFFFDAD6)
    val onErrorContainer = Color(0xFF410002)

    // ── background / surface palette ────────────────────────────
    val background = Color(0xFFFAFAFA)
    val onBackground = Color(0xFF1A1A1A)
    val surface = Color(0xFFFFFFFF)
    val onSurface = Color(0xFF1A1A1A)
    val surfaceVariant = Color(0xFFF3F3F3)
    val onSurfaceVariant = Color(0xFF4A4458)

    // ── surface elevation tones ─────────────────────────────────
    val surfaceBright = Color(0xFFFFFFFF)
    val surfaceDim = Color(0xFFE8E8E8)
    val surfaceContainer = Color(0xFFF3F3F3)
    val surfaceContainerHigh = Color(0xFFECECEC)
    val surfaceContainerHighest = Color(0xFFE5E5E5)
    val surfaceContainerLow = Color(0xFFF7F7F7)
    val surfaceContainerLowest = Color(0xFFFFFFFF)

    // ── outline / scrim ─────────────────────────────────────────
    val outline = Color(0xFF7A7289)
    val outlineVariant = Color(0xFFCBC2DB)
    val scrim = Color(0xFF000000)

    // ── meeting-specific status colours ─────────────────────────
    val eventOnline = Color(0xFF1DC0A6)
    val eventOffline = Color(0xFF5A6A8E)
    val eventFinished = Color(0xFFCCCCCC)

    // ── tag colours ─────────────────────────────────────────────
    val tagBackground = Color(0xFFF3D9FF)
    val tagContent = Color(0xFF9000D4)
}
