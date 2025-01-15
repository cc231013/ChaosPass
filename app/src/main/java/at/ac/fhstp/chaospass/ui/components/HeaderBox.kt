package at.ac.fhstp.chaospass.ui.components


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun HeaderBox(icon: ImageVector, rotation: Float = 0f, backgroundColor: Color) {
    Card(
        modifier = Modifier
            .size(150.dp), // Square size
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = backgroundColor) // Use Color for the Card
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = "Header Icon",
                tint = Color.Black,
                modifier = Modifier
                    .size(164.dp)
                    .rotate(rotation)
            )
        }
    }
}
