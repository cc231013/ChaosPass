package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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

@Composable
fun LogoIcon() {
    Box(
        modifier = Modifier
            .size(48.dp) // Adjust the size of the logo
            .background(Color.Transparent, shape = MaterialTheme.shapes.small),
        contentAlignment = Alignment.Center
    ) {
        // Lock icon in the background
        Icon(
            imageVector = Icons.Default.Lock, // Replace with your lock icon
            contentDescription = "Logo Lock",
            tint = Color.Black,
            modifier = Modifier.size(48.dp)
        )

        // Letters on top of the lock icon
        Text(
            text = "cP",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
