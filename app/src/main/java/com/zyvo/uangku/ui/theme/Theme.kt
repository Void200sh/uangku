package com.zyvo.uangku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/** Colors that are not part of the Material scheme but belong to the brand. */
data class Brand(
    val income: Color,
    val expense: Color,
    val heroTop: Color,
    val heroBottom: Color,
    val soft: Color
)

val LocalBrand = staticCompositionLocalOf {
    Brand(
        income = Color(0xFF1B7A43),
        expense = Color(0xFFE0453A),
        heroTop = Color(0xFF1F8A4C),
        heroBottom = Color(0xFF136437),
        soft = Color(0xFFE4F0E8)
    )
}

private val LightColors = lightColorScheme(
    primary = Color(0xFF1B7A43),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCF0E3),
    onPrimaryContainer = Color(0xFF0B3A20),
    secondary = Color(0xFF52705C),
    onSecondary = Color.White,
    background = Color(0xFFF6F7F9),
    onBackground = Color(0xFF1A1A1A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1A1A),
    surfaceVariant = Color(0xFFF2F3F5),
    onSurfaceVariant = Color(0xFF8A8A8E),
    outline = Color(0xFFE2E4E8),
    outlineVariant = Color(0xFFEFEFF0),
    error = Color(0xFFE0453A),
    onError = Color.White
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF4ADE80),
    onPrimary = Color(0xFF06280F),
    primaryContainer = Color(0xFF14361F),
    onPrimaryContainer = Color(0xFFB7F5CE),
    secondary = Color(0xFF8FC9A6),
    onSecondary = Color(0xFF06280F),
    background = Color(0xFF121214),
    onBackground = Color(0xFFF2F2F4),
    surface = Color(0xFF1E1E20),
    onSurface = Color(0xFFF2F2F4),
    surfaceVariant = Color(0xFF26262A),
    onSurfaceVariant = Color(0xFF9A9AA0),
    outline = Color(0xFF33333A),
    outlineVariant = Color(0xFF2A2A2E),
    error = Color(0xFFFF8A7A),
    onError = Color(0xFF3A0B06)
)

@Composable
fun UangKuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val brand = if (darkTheme) {
        Brand(
            income = Color(0xFF57E39B),
            expense = Color(0xFFFF8A7A),
            heroTop = Color(0xFF1B6B3E),
            heroBottom = Color(0xFF0B3A22),
            soft = Color(0xFF1B2E22)
        )
    } else {
        Brand(
            income = Color(0xFF1B7A43),
            expense = Color(0xFFE0453A),
            heroTop = Color(0xFF1F8A4C),
            heroBottom = Color(0xFF136437),
            soft = Color(0xFFE4F0E8)
        )
    }

    CompositionLocalProvider(LocalBrand provides brand) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColors else LightColors,
            content = content
        )
    }
}
