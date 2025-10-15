package com.example.previsaodotempo.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Paleta forte e sofisticada
private val Purple800 = Color(0xFF4A148C)
private val Indigo500 = Color(0xFF3F51B5)
private val Cyan300 = Color(0xFF4DD0E1)
private val DeepBlue = Color(0xFF081A51)
private val CardGradientStart = Color(0xFF7C4DFF)
private val CardGradientEnd = Color(0xFF00BCD4)
private val Accent = Color(0xFFFFC107)
private val SurfaceDark = Color(0xFF0B1020)

private val LightColors = lightColors(
    primary = Indigo500,
    primaryVariant = Purple800,
    secondary = Cyan300,
    background = Color(0xFFF5F7FB),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color.Black,
    error = Color(0xFFB00020)
)

private val DarkColors = darkColors(
    primary = CardGradientStart,
    primaryVariant = CardGradientEnd,
    secondary = Accent,
    background = SurfaceDark,
    surface = Color(0xFF0F1724),
    onPrimary = Color.Black,
    onBackground = Color.White,
    error = Color(0xFFCF6679)
)

private val Typography = Typography(
    h5 = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp),
    subtitle1 = androidx.compose.ui.text.TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp),
    body1 = androidx.compose.ui.text.TextStyle(fontSize = 14.sp)
)

@Composable
fun WeatherPeekTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes(),
        content = content
    )
}
