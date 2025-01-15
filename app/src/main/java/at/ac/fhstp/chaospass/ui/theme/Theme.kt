package at.ac.fhstp.chaospass.ui.theme


import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val NormalColorScheme = lightColorScheme(
    primary = BackgroundNavy,
    background = BackgroundBlue,
    onBackground = BackgroundNavy,

)

private val ChaosColorScheme = darkColorScheme(
    primary = BackgroundBlue,
    background = ChaosBackground,
    onBackground = BackgroundBlue
)

@Composable
fun ChaosPassTheme(
    chaosModeEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (chaosModeEnabled) ChaosColorScheme else NormalColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
