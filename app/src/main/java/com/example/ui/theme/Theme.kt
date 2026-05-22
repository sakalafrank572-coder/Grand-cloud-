package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.graphics.Color

private val AuraDarkColorScheme = darkColorScheme(
    primary = GoldenAccent,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1D1B20),
    onPrimaryContainer = GoldenAccent,
    secondary = CyberPurple,
    onSecondary = Color.White,
    tertiary = CyberCyan,
    onTertiary = Color.Black,
    background = ObsidianBackground,
    onBackground = Color.White,
    surface = LuxuryDarkCard,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF18181F),
    onSurfaceVariant = TextSecondary,
    outline = LuxuryDarkCardBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark luxury theme
    dynamicColor: Boolean = false, // Use our handcrafted luxury palette
    content: @Composable () -> Unit,
) {
    val colorScheme = AuraDarkColorScheme

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
