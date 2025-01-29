package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color.Transparent, shape = MaterialTheme.shapes.small)
            .clickable { showDialog = true }, // Make the icon clickable
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
                .offset(y = 8.dp)
        )
    }

    // Dialog for App Information
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("About ChaosPass") },
            text = {
                Text(
                    "Welcome to ChaosPass, a password manager that stores your passwords safely in a local encrypted database. " +
                            "Try out ChaosMode if you want a little excitement and challenge when accessing your passwords!"
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
