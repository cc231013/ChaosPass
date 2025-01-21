package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.fhstp.chaospass.utils.copyToClipboard

@Composable
fun InfoField(
    label: String,
    value: String,
    isPasswordField: Boolean = false,
    labelColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    borderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isPasswordVisible = remember { mutableStateOf(false) } // This state controls visibility

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                copyToClipboard(context, label, value)
            }
    ) {
        // Display the label
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                color = labelColor,
                fontSize = 12.sp
            ),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        // Display the value with a subtle border
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = borderColor, shape = MaterialTheme.shapes.small)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Display the value or obscured password
            Text(
                text = if (isPasswordField && !isPasswordVisible.value) "••••••••" else value,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.weight(1f)
            )

            // Add the copy icon
            IconButton(
                onClick = { copyToClipboard(context, label, value) },
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy $label",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }

            // Add visibility toggle for password fields
            if (isPasswordField) {
                IconButton(
                    onClick = {
                        isPasswordVisible.value = !isPasswordVisible.value // Toggle visibility state
                    },
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible.value) "Hide Password" else "Show Password",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
