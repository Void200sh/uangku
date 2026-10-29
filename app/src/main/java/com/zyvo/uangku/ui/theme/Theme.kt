package com.zyvo.uangku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
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

/** Palet warna yang bisa dipilih user di Pengaturan. */
enum class AppTheme(val id: String, val label: String, val tagline: String) {
    EMERALD("emerald", "Emerald", "Hijau tenang"),
    PIXEL("pixel", "Pixel", "Biru bersih"),
    SUNSET("sunset", "Senja", "Oranye hangat"),
    LAVENDER("lavender", "Lavender", "Ungu lembut");

    companion object {
        fun fromId(id: String?): AppTheme = values().firstOrNull { it.id == id } ?: EMERALD
    }
}

private class ThemeSpec(
    val light: ColorScheme,
    val dark: ColorScheme,
    val brandLight: Brand,
    val brandDark: Brand
)

private val EMERALD = ThemeSpec(
    light = lightColorScheme(
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
    ),
    dark = darkColorScheme(
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
    ),
    brandLight = Brand(
        income = Color(0xFF1B7A43),
        expense = Color(0xFFE0453A),
        heroTop = Color(0xFF1F8A4C),
        heroBottom = Color(0xFF136437),
        soft = Color(0xFFE4F0E8)
    ),
    brandDark = Brand(
        income = Color(0xFF57E39B),
        expense = Color(0xFFFF8A7A),
        heroTop = Color(0xFF1B6B3E),
        heroBottom = Color(0xFF0B3A22),
        soft = Color(0xFF1B2E22)
    )
)

private val PIXEL = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFF1A73E8),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD3E3FD),
        onPrimaryContainer = Color(0xFF062E6F),
        secondary = Color(0xFF5F6368),
        onSecondary = Color.White,
        background = Color(0xFFF8F9FA),
        onBackground = Color(0xFF1F1F1F),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1F1F1F),
        surfaceVariant = Color(0xFFF1F3F4),
        onSurfaceVariant = Color(0xFF6B7075),
        outline = Color(0xFFDADCE0),
        outlineVariant = Color(0xFFE8EAED),
        error = Color(0xFFD93025),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFF8AB4F8),
        onPrimary = Color(0xFF062E6F),
        primaryContainer = Color(0xFF0842A0),
        onPrimaryContainer = Color(0xFFD3E3FD),
        secondary = Color(0xFFBDC1C6),
        onSecondary = Color(0xFF202124),
        background = Color(0xFF121212),
        onBackground = Color(0xFFE8EAED),
        surface = Color(0xFF1F1F1F),
        onSurface = Color(0xFFE8EAED),
        surfaceVariant = Color(0xFF2D2E30),
        onSurfaceVariant = Color(0xFF9AA0A6),
        outline = Color(0xFF3C4043),
        outlineVariant = Color(0xFF2A2B2D),
        error = Color(0xFFF28B82),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF1E8E3E),
        expense = Color(0xFFD93025),
        heroTop = Color(0xFF1A73E8),
        heroBottom = Color(0xFF0B57D0),
        soft = Color(0xFFE8F0FE)
    ),
    brandDark = Brand(
        income = Color(0xFF81C995),
        expense = Color(0xFFF28B82),
        heroTop = Color(0xFF1A73E8),
        heroBottom = Color(0xFF0842A0),
        soft = Color(0xFF1B2A3D)
    )
)

private val SUNSET = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFFC2410C),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFFE5D9),
        onPrimaryContainer = Color(0xFF5A1B00),
        secondary = Color(0xFF8D6E63),
        onSecondary = Color.White,
        background = Color(0xFFFFF8F4),
        onBackground = Color(0xFF231A15),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF231A15),
        surfaceVariant = Color(0xFFF8EFE9),
        onSurfaceVariant = Color(0xFF8A7166),
        outline = Color(0xFFEADDD4),
        outlineVariant = Color(0xFFF2E9E2),
        error = Color(0xFFB3261E),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFFFFB59B),
        onPrimary = Color(0xFF5A1B00),
        primaryContainer = Color(0xFF7A2E00),
        onPrimaryContainer = Color(0xFFFFDBCB),
        secondary = Color(0xFFD8BFB4),
        onSecondary = Color(0xFF3A2A22),
        background = Color(0xFF17110E),
        onBackground = Color(0xFFF4E4DC),
        surface = Color(0xFF221A16),
        onSurface = Color(0xFFF4E4DC),
        surfaceVariant = Color(0xFF2E231D),
        onSurfaceVariant = Color(0xFFB9A196),
        outline = Color(0xFF453229),
        outlineVariant = Color(0xFF33251E),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF2E7D32),
        expense = Color(0xFFD84315),
        heroTop = Color(0xFFF4511E),
        heroBottom = Color(0xFFBF360C),
        soft = Color(0xFFFDE7DC)
    ),
    brandDark = Brand(
        income = Color(0xFF8BD48F),
        expense = Color(0xFFFFB59B),
        heroTop = Color(0xFFF4511E),
        heroBottom = Color(0xFF8C2A00),
        soft = Color(0xFF33221A)
    )
)

private val LAVENDER = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFF6D28D9),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFEDE4FF),
        onPrimaryContainer = Color(0xFF2E1065),
        secondary = Color(0xFF776E8A),
        onSecondary = Color.White,
        background = Color(0xFFF9F7FE),
        onBackground = Color(0xFF1C1922),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF1C1922),
        surfaceVariant = Color(0xFFF1ECFA),
        onSurfaceVariant = Color(0xFF7D7591),
        outline = Color(0xFFE4DCF2),
        outlineVariant = Color(0xFFEFE9F8),
        error = Color(0xFFD93025),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFFC4B5FD),
        onPrimary = Color(0xFF2E1065),
        primaryContainer = Color(0xFF3B1D75),
        onPrimaryContainer = Color(0xFFE9DDFF),
        secondary = Color(0xFFB7AECB),
        onSecondary = Color(0xFF221D2E),
        background = Color(0xFF121015),
        onBackground = Color(0xFFEEEAF6),
        surface = Color(0xFF1C1922),
        onSurface = Color(0xFFEEEAF6),
        surfaceVariant = Color(0xFF272232),
        onSurfaceVariant = Color(0xFFA79FBB),
        outline = Color(0xFF383144),
        outlineVariant = Color(0xFF2A2433),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF2E9E5B),
        expense = Color(0xFFE0457B),
        heroTop = Color(0xFF7C3AED),
        heroBottom = Color(0xFF5B21B6),
        soft = Color(0xFFEFE7FD)
    ),
    brandDark = Brand(
        income = Color(0xFF7BE0A0),
        expense = Color(0xFFFFA3C4),
        heroTop = Color(0xFF7C3AED),
        heroBottom = Color(0xFF3B1D75),
        soft = Color(0xFF271E35)
    )
)

private val specs: Map<AppTheme, ThemeSpec> = mapOf(
    AppTheme.EMERALD to EMERALD,
    AppTheme.PIXEL to PIXEL,
    AppTheme.SUNSET to SUNSET,
    AppTheme.LAVENDER to LAVENDER
)

/** Warna gradien hero untuk pratinjau tema di Pengaturan. */
fun themeHero(theme: AppTheme, dark: Boolean): Pair<Color, Color> {
    val brand = if (dark) specs.getValue(theme).brandDark else specs.getValue(theme).brandLight
    return brand.heroTop to brand.heroBottom
}

@Composable
fun UangKuTheme(
    theme: AppTheme = AppTheme.EMERALD,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val spec = specs.getValue(theme)
    val brand = if (darkTheme) spec.brandDark else spec.brandLight

    CompositionLocalProvider(LocalBrand provides brand) {
        MaterialTheme(
            colorScheme = if (darkTheme) spec.dark else spec.light,
            content = content
        )
    }
}
