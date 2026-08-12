package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FantasyColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = ObsidianBg,
    primaryContainer = ParchmentSurface,
    onPrimaryContainer = GoldSecondary,
    secondary = GoldSecondary,
    onSecondary = ObsidianBg,
    background = ObsidianBg,
    onBackground = TextParchment,
    surface = ParchmentSurface,
    onSurface = TextParchment,
    surfaceVariant = ParchmentBorder,
    onSurfaceVariant = TextMuted,
    outline = GoldPrimary
)

@Composable
fun FantasyRpgTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FantasyColorScheme,
        typography = Typography,
        content = content
    )
}
