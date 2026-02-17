package avinash.app.mystocks.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ── Material3 Color Schemes ──

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = Color.White,
    secondary = DarkSecondary,
    onSecondary = Color.White,
    tertiary = DarkDestructive,
    background = DarkBackground,
    onBackground = DarkForeground,
    surface = DarkSurface,
    onSurface = DarkForeground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkMutedForeground,
    outline = DarkOutline,
    outlineVariant = DarkBorder,
    error = DarkDestructive,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = Color.White,
    secondary = LightSecondary,
    onSecondary = Color.White,
    tertiary = LightDestructive,
    background = LightBackground,
    onBackground = LightForeground,
    surface = LightSurface,
    onSurface = LightForeground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightMutedForeground,
    outline = LightOutline,
    outlineVariant = LightBorder,
    error = LightDestructive,
    onError = Color.White
)

// ── Extended Colors (stock-specific semantic tokens) ──

@Immutable
data class ExtendedColorScheme(
    val stockUp: Color,
    val stockDown: Color,
    val warning: Color,
    val info: Color,
    val cardBackground: Color,
    val cardBorder: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val successContainer: Color,
    val destructiveContainer: Color,
    val surfaceElevated: Color,
    val gradientStart: Color,
    val gradientEnd: Color
)

private val DarkExtendedColors = ExtendedColorScheme(
    stockUp = Color(0xFFFFEB3B),
    stockDown = DarkDestructive,
    warning = Color(0xFFFBBF24),
    info = Color(0xFF58A6FF),
    cardBackground = DarkCard,
    cardBorder = DarkOutline,
    textPrimary = DarkForeground,
    textSecondary = DarkMutedForeground,
    successContainer = Color(0xFF1A3D2E),
    destructiveContainer = Color(0xFF3D1A1A),
    surfaceElevated = Color(0xFF1E1E35),
    gradientStart = DarkBackground,
    gradientEnd = DarkSurface
)

private val LightExtendedColors = ExtendedColorScheme(
    stockUp = Color(0xFFFBC02D),
    stockDown = LightDestructive,
    warning = Color(0xFFF59E0B),
    info = Color(0xFF3B82F6),
    cardBackground = LightCard,
    cardBorder = LightOutline,
    textPrimary = LightForeground,
    textSecondary = LightMutedForeground,
    successContainer = Color(0xFFD1FAE5),
    destructiveContainer = Color(0xFFFEE2E2),
    surfaceElevated = Color(0xFFF9FAFB),
    gradientStart = LightBackground,
    gradientEnd = LightSurface
)

val LocalExtendedColors = staticCompositionLocalOf { DarkExtendedColors }

@Composable
fun MyStocksTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(LocalExtendedColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object AppTheme {
    val extendedColors: ExtendedColorScheme
        @Composable
        get() = LocalExtendedColors.current
}
