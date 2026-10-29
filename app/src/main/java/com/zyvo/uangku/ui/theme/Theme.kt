package com.zyvo.uangku.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

/** Token desain yang membedakan tiap gaya (bukan cuma warna). */
data class Look(
    val style: AppStyle,
    val radius: Dp,
    val heroRadius: Dp,
    val border: Dp,
    val hardShadow: Dp,
    val dashed: Boolean,
    val circleIcons: Boolean,
    val rowDivider: Boolean,
    val mono: Boolean,
    val sharpCharts: Boolean,
    val fabCircle: Boolean
)

val LocalLook = staticCompositionLocalOf {
    Look(
        style = AppStyle.LEDGER,
        radius = 20.dp,
        heroRadius = 26.dp,
        border = 0.dp,
        hardShadow = 0.dp,
        dashed = false,
        circleIcons = false,
        rowDivider = false,
        mono = false,
        sharpCharts = false,
        fabCircle = true
    )
}

/** Gaya tampilan — mengikuti 3 arah desain preview + satu tambahan. */
enum class AppStyle(val id: String, val label: String, val tagline: String) {
    LEDGER("ledger", "Ledger", "Bersih & rapi"),
    NIGHT("night", "Night Mint", "Gelap & neon"),
    SAND("sand", "Sand Wallet", "Hangat & tebal"),
    PIXEL("pixel", "Pixel", "Tajam & retro");

    companion object {
        private val legacy = mapOf(
            "emerald" to LEDGER,
            "sunset" to SAND,
            "lavender" to NIGHT
        )

        fun fromId(id: String?): AppStyle =
            values().firstOrNull { it.id == id } ?: id?.let { legacy[it] } ?: LEDGER
    }
}

/** Warna aksen yang bisa dipilih terpisah dari gaya. */
enum class Accent(val id: String, val label: String) {
    AUTO("auto", "Asli"),
    HIJAU("hijau", "Hijau"),
    BIRU("biru", "Biru"),
    ORANYE("oranye", "Oranye"),
    UNGU("ungu", "Ungu"),
    PINK("pink", "Pink");

    companion object {
        fun fromId(id: String?): Accent = values().firstOrNull { it.id == id } ?: AUTO
    }
}

private class AccentColors(
    val lightPrimary: Color,
    val lightOnPrimary: Color,
    val lightContainer: Color,
    val lightOnContainer: Color,
    val lightHeroTop: Color,
    val lightHeroBottom: Color,
    val lightSoft: Color,
    val darkPrimary: Color,
    val darkOnPrimary: Color,
    val darkContainer: Color,
    val darkOnContainer: Color,
    val darkHeroTop: Color,
    val darkHeroBottom: Color,
    val darkSoft: Color
)

private val ACCENTS: Map<Accent, AccentColors> = mapOf(
    Accent.HIJAU to AccentColors(
        lightPrimary = Color(0xFF1B7A43),
        lightOnPrimary = Color.White,
        lightContainer = Color(0xFFDCF0E3),
        lightOnContainer = Color(0xFF0B3A20),
        lightHeroTop = Color(0xFF1F8A4C),
        lightHeroBottom = Color(0xFF136437),
        lightSoft = Color(0xFFE4F0E8),
        darkPrimary = Color(0xFF4ADE80),
        darkOnPrimary = Color(0xFF06280F),
        darkContainer = Color(0xFF14361F),
        darkOnContainer = Color(0xFFB7F5CE),
        darkHeroTop = Color(0xFF1B6B3E),
        darkHeroBottom = Color(0xFF0B3A22),
        darkSoft = Color(0xFF1B2E22)
    ),
    Accent.BIRU to AccentColors(
        lightPrimary = Color(0xFF1A73E8),
        lightOnPrimary = Color.White,
        lightContainer = Color(0xFFD3E3FD),
        lightOnContainer = Color(0xFF062E6F),
        lightHeroTop = Color(0xFF1A73E8),
        lightHeroBottom = Color(0xFF0B57D0),
        lightSoft = Color(0xFFE8F0FE),
        darkPrimary = Color(0xFF8AB4F8),
        darkOnPrimary = Color(0xFF062E6F),
        darkContainer = Color(0xFF0842A0),
        darkOnContainer = Color(0xFFD3E3FD),
        darkHeroTop = Color(0xFF1A73E8),
        darkHeroBottom = Color(0xFF0842A0),
        darkSoft = Color(0xFF1B2A3D)
    ),
    Accent.ORANYE to AccentColors(
        lightPrimary = Color(0xFFC2410C),
        lightOnPrimary = Color.White,
        lightContainer = Color(0xFFFFE5D9),
        lightOnContainer = Color(0xFF5A1B00),
        lightHeroTop = Color(0xFFF4511E),
        lightHeroBottom = Color(0xFFBF360C),
        lightSoft = Color(0xFFFDE7DC),
        darkPrimary = Color(0xFFFFB59B),
        darkOnPrimary = Color(0xFF5A1B00),
        darkContainer = Color(0xFF7A2E00),
        darkOnContainer = Color(0xFFFFDBCB),
        darkHeroTop = Color(0xFFF4511E),
        darkHeroBottom = Color(0xFF8C2A00),
        darkSoft = Color(0xFF33221A)
    ),
    Accent.UNGU to AccentColors(
        lightPrimary = Color(0xFF6D28D9),
        lightOnPrimary = Color.White,
        lightContainer = Color(0xFFEDE4FF),
        lightOnContainer = Color(0xFF2E1065),
        lightHeroTop = Color(0xFF7C3AED),
        lightHeroBottom = Color(0xFF5B21B6),
        lightSoft = Color(0xFFEFE7FD),
        darkPrimary = Color(0xFFC4B5FD),
        darkOnPrimary = Color(0xFF2E1065),
        darkContainer = Color(0xFF3B1D75),
        darkOnContainer = Color(0xFFE9DDFF),
        darkHeroTop = Color(0xFF7C3AED),
        darkHeroBottom = Color(0xFF3B1D75),
        darkSoft = Color(0xFF271E35)
    ),
    Accent.PINK to AccentColors(
        lightPrimary = Color(0xFFBE185D),
        lightOnPrimary = Color.White,
        lightContainer = Color(0xFFFCE7F0),
        lightOnContainer = Color(0xFF500724),
        lightHeroTop = Color(0xFFEC4899),
        lightHeroBottom = Color(0xFFBE185D),
        lightSoft = Color(0xFFFCE7F3),
        darkPrimary = Color(0xFFF9A8D4),
        darkOnPrimary = Color(0xFF500724),
        darkContainer = Color(0xFF831843),
        darkOnContainer = Color(0xFFFCE7F0),
        darkHeroTop = Color(0xFFEC4899),
        darkHeroBottom = Color(0xFF831843),
        darkSoft = Color(0xFF33202B)
    )
)

private class ThemeSpec(
    val light: ColorScheme,
    val dark: ColorScheme,
    val brandLight: Brand,
    val brandDark: Brand,
    val look: Look
)

private val LEDGER = ThemeSpec(
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
    ),
    look = Look(
        style = AppStyle.LEDGER,
        radius = 20.dp,
        heroRadius = 26.dp,
        border = 0.dp,
        hardShadow = 0.dp,
        dashed = false,
        circleIcons = false,
        rowDivider = false,
        mono = false,
        sharpCharts = false,
        fabCircle = true
    )
)

private val NIGHT = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFF0B8F55),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFD8F5E5),
        onPrimaryContainer = Color(0xFF052F1C),
        secondary = Color(0xFF6D7C74),
        onSecondary = Color.White,
        background = Color(0xFFF3FAF6),
        onBackground = Color(0xFF0E1110),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF0E1110),
        surfaceVariant = Color(0xFFEAF4EE),
        onSurfaceVariant = Color(0xFF6D7C74),
        outline = Color(0xFFDCE8E1),
        outlineVariant = Color(0xFFE9F2ED),
        error = Color(0xFFD93025),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFF37E08C),
        onPrimary = Color(0xFF08130D),
        primaryContainer = Color(0xFF12301F),
        onPrimaryContainer = Color(0xFFA8F5CB),
        secondary = Color(0xFF7C8A83),
        onSecondary = Color(0xFF0E1110),
        background = Color(0xFF0E1110),
        onBackground = Color(0xFFEAF3EE),
        surface = Color(0xFF141A17),
        onSurface = Color(0xFFEAF3EE),
        surfaceVariant = Color(0xFF1B2320),
        onSurfaceVariant = Color(0xFF7C8A83),
        outline = Color(0xFF26332B),
        outlineVariant = Color(0xFF1E2A24),
        error = Color(0xFFF0796B),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF0B8F55),
        expense = Color(0xFFD93025),
        heroTop = Color(0xFF0B8F55),
        heroBottom = Color(0xFF0B8F55),
        soft = Color(0xFFD8F5E5)
    ),
    brandDark = Brand(
        income = Color(0xFF37E08C),
        expense = Color(0xFFF0796B),
        heroTop = Color(0xFF37E08C),
        heroBottom = Color(0xFF37E08C),
        soft = Color(0xFF12301F)
    ),
    look = Look(
        style = AppStyle.NIGHT,
        radius = 16.dp,
        heroRadius = 20.dp,
        border = 1.dp,
        hardShadow = 0.dp,
        dashed = false,
        circleIcons = true,
        rowDivider = true,
        mono = false,
        sharpCharts = false,
        fabCircle = false
    )
)

private val SAND = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFFC2410C),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFFBE0CC),
        onPrimaryContainer = Color(0xFF4A1E05),
        secondary = Color(0xFF8D6E63),
        onSecondary = Color.White,
        background = Color(0xFFFBF4EA),
        onBackground = Color(0xFF2A1C12),
        surface = Color(0xFFFFFDF9),
        onSurface = Color(0xFF2A1C12),
        surfaceVariant = Color(0xFFF4EADB),
        onSurfaceVariant = Color(0xFFA08A76),
        outline = Color(0xFFD9C6B2),
        outlineVariant = Color(0xFFEFE3D4),
        error = Color(0xFFB3261E),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFFF2B27C),
        onPrimary = Color(0xFF2A1C12),
        primaryContainer = Color(0xFF4A2E17),
        onPrimaryContainer = Color(0xFFFFE2C9),
        secondary = Color(0xFFD8BFB4),
        onSecondary = Color(0xFF3A2A22),
        background = Color(0xFF17110C),
        onBackground = Color(0xFFF7E9D8),
        surface = Color(0xFF221A12),
        onSurface = Color(0xFFF7E9D8),
        surfaceVariant = Color(0xFF2C2117),
        onSurfaceVariant = Color(0xFFB99F86),
        outline = Color(0xFF4A3928),
        outlineVariant = Color(0xFF33251A),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF1B7A43),
        expense = Color(0xFFC2410C),
        heroTop = Color(0xFFF2B27C),
        heroBottom = Color(0xFFF2B27C),
        soft = Color(0xFFFBF0E3)
    ),
    brandDark = Brand(
        income = Color(0xFF8BD48F),
        expense = Color(0xFFFFB59B),
        heroTop = Color(0xFFF2B27C),
        heroBottom = Color(0xFFF2B27C),
        soft = Color(0xFF2C2117)
    ),
    look = Look(
        style = AppStyle.SAND,
        radius = 16.dp,
        heroRadius = 20.dp,
        border = 2.dp,
        hardShadow = 4.dp,
        dashed = true,
        circleIcons = false,
        rowDivider = false,
        mono = false,
        sharpCharts = false,
        fabCircle = false
    )
)

private val PIXEL = ThemeSpec(
    light = lightColorScheme(
        primary = Color(0xFF2563EB),
        onPrimary = Color.White,
        primaryContainer = Color(0xFFDBE4FF),
        onPrimaryContainer = Color(0xFF0B1F5C),
        secondary = Color(0xFF5F6368),
        onSecondary = Color.White,
        background = Color(0xFFF5F7FF),
        onBackground = Color(0xFF101322),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF101322),
        surfaceVariant = Color(0xFFEDF0F9),
        onSurfaceVariant = Color(0xFF6B7086),
        outline = Color(0xFFCBD2E6),
        outlineVariant = Color(0xFFE3E7F4),
        error = Color(0xFFD93025),
        onError = Color.White
    ),
    dark = darkColorScheme(
        primary = Color(0xFF7EA2FF),
        onPrimary = Color(0xFF0B1F5C),
        primaryContainer = Color(0xFF1A2C63),
        onPrimaryContainer = Color(0xFFDBE4FF),
        secondary = Color(0xFFB7BDD0),
        onSecondary = Color(0xFF101322),
        background = Color(0xFF0B1020),
        onBackground = Color(0xFFE7EAF6),
        surface = Color(0xFF121A33),
        onSurface = Color(0xFFE7EAF6),
        surfaceVariant = Color(0xFF1A2340),
        onSurfaceVariant = Color(0xFF98A3C4),
        outline = Color(0xFF2B3763),
        outlineVariant = Color(0xFF1F2A4C),
        error = Color(0xFFF28B82),
        onError = Color(0xFF3A0B06)
    ),
    brandLight = Brand(
        income = Color(0xFF1E8E3E),
        expense = Color(0xFFD93025),
        heroTop = Color(0xFF2563EB),
        heroBottom = Color(0xFF173FA8),
        soft = Color(0xFFDBE4FF)
    ),
    brandDark = Brand(
        income = Color(0xFF81C995),
        expense = Color(0xFFF28B82),
        heroTop = Color(0xFF2563EB),
        heroBottom = Color(0xFF0B1F5C),
        soft = Color(0xFF14204A)
    ),
    look = Look(
        style = AppStyle.PIXEL,
        radius = 8.dp,
        heroRadius = 10.dp,
        border = 2.dp,
        hardShadow = 0.dp,
        dashed = false,
        circleIcons = false,
        rowDivider = true,
        mono = true,
        sharpCharts = true,
        fabCircle = false
    )
)

private val specs: Map<AppStyle, ThemeSpec> = mapOf(
    AppStyle.LEDGER to LEDGER,
    AppStyle.NIGHT to NIGHT,
    AppStyle.SAND to SAND,
    AppStyle.PIXEL to PIXEL
)

/** Warna hero untuk pratinjau gaya di Pengaturan. */
fun styleHero(style: AppStyle, accent: Accent, dark: Boolean): Pair<Color, Color> {
    val spec = specs.getValue(style)
    val a = ACCENTS[accent]
    if (a == null || accent == Accent.AUTO) {
        return if (dark) {
            spec.brandDark.heroTop to spec.brandDark.heroBottom
        } else {
            spec.brandLight.heroTop to spec.brandLight.heroBottom
        }
    }
    return if (dark) a.darkHeroTop to a.darkHeroBottom else a.lightHeroTop to a.lightHeroBottom
}

/** Warna contoh aksen; null = ikut gaya (auto). */
fun accentSwatch(accent: Accent, dark: Boolean): Color? {
    val a = ACCENTS[accent] ?: return null
    return if (dark) a.darkPrimary else a.lightPrimary
}

@Composable
fun UangKuTheme(
    style: AppStyle = AppStyle.LEDGER,
    accent: Accent = Accent.AUTO,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val spec = specs.getValue(style)
    var scheme = if (darkTheme) spec.dark else spec.light
    var brand = if (darkTheme) spec.brandDark else spec.brandLight

    val accentColors = ACCENTS[accent]
    if (accentColors != null && accent != Accent.AUTO) {
        scheme = if (darkTheme) {
            scheme.copy(
                primary = accentColors.darkPrimary,
                onPrimary = accentColors.darkOnPrimary,
                primaryContainer = accentColors.darkContainer,
                onPrimaryContainer = accentColors.darkOnContainer
            )
        } else {
            scheme.copy(
                primary = accentColors.lightPrimary,
                onPrimary = accentColors.lightOnPrimary,
                primaryContainer = accentColors.lightContainer,
                onPrimaryContainer = accentColors.lightOnContainer
            )
        }
        brand = if (darkTheme) {
            brand.copy(
                heroTop = accentColors.darkHeroTop,
                heroBottom = accentColors.darkHeroBottom,
                soft = accentColors.darkSoft
            )
        } else {
            brand.copy(
                heroTop = accentColors.lightHeroTop,
                heroBottom = accentColors.lightHeroBottom,
                soft = accentColors.lightSoft
            )
        }
    }

    val shapes = Shapes(
        extraSmall = RoundedCornerShape(spec.look.radius * 0.4f),
        small = RoundedCornerShape(spec.look.radius * 0.7f),
        medium = RoundedCornerShape(spec.look.radius),
        large = RoundedCornerShape(spec.look.heroRadius),
        extraLarge = RoundedCornerShape(spec.look.heroRadius)
    )

    CompositionLocalProvider(LocalBrand provides brand, LocalLook provides spec.look) {
        MaterialTheme(
            colorScheme = scheme,
            shapes = shapes,
            content = content
        )
    }
}
