package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue

@Composable
fun LogoIcon(chaosModeEnabled: Boolean) {
    // Colors and text based on chaos mode
    val lockColor = if (chaosModeEnabled) ChaosAddBlue else Color.Black
    val textColor = if (chaosModeEnabled) Color.Black else Color.White
    val backgroundColor = if (chaosModeEnabled) ChaosAddBlue else Color.Black
    val logoText = if (chaosModeEnabled) "Cp" else "cP"

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Transparent, shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        // Background Box
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(backgroundColor)
        )

        // Lock Icon
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = "Logo Lock",
            tint = lockColor,
            modifier = Modifier.size(48.dp)
        )

        // Text inside the lock icon
        Text(
            text = logoText,
            color = textColor,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 8.dp) // Adjust position if necessary
        )
    }
}
