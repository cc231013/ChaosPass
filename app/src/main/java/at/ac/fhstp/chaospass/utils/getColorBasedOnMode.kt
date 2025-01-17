package at.ac.fhstp.chaospass.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getColorBasedOnMode(
    chaosModeEnabled: Boolean,
    defaultColor: Color,
    chaosColor: Color
): Color {
    return if (chaosModeEnabled) chaosColor else defaultColor
}
