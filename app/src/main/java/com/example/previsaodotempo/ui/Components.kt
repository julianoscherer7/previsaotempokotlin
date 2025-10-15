import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WeatherCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

@Composable
fun IconCircle(icon: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.08f),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

// Correção de "lerpColor" (sem erro de digitação e funcional)
private fun interpolateColor(a: Color, b: Color, t: Float): Color {
    val r = (a.red + (b.red - a.red) * t)
    val g = (a.green + (b.green - a.green) * t)
    val bl = (a.blue + (b.blue - a.blue) * t)
    val alpha = (a.alpha + (b.alpha - a.alpha) * t)
    return Color(red = r, green = g, blue = bl, alpha = alpha)
}
