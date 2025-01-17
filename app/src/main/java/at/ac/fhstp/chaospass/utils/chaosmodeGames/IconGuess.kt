package at.ac.fhstp.chaospass.utils.chaosmodeGames

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.theme.ChaosAccept
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnBlack
import at.ac.fhstp.chaospass.utils.copyToClipboard
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode
import kotlin.random.Random

@Composable
fun IconGuess(
    entry: Entry?,
    chaosModeEnabled: Boolean,
    onPasswordRevealed: () -> Unit
) {
    val totalIcons = 6
    var correctIconIndex by remember { mutableStateOf(Random.nextInt(totalIcons)) }
    var selectedIconIndex by remember { mutableStateOf(-1) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordFieldVisible by remember { mutableStateOf(false) } // New state for password field visibility
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display feedback text
        Text(
            text = when {
                selectedIconIndex == -1 -> "Find the correct icon to reveal the password!"
                selectedIconIndex == correctIconIndex -> "Correct! Password revealed."
                else -> "Wrong icon, try again!"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = getColorBasedOnMode(
                chaosModeEnabled,
                MaterialTheme.colorScheme.onSurface,
                ChaosOnBlack
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Show the password field only when the correct icon is clicked
        if (isPasswordFieldVisible) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoField(
                    label = "Password",
                    value = if (isPasswordVisible) entry?.password.orEmpty() else "••••••••",
                    backgroundColor = getColorBasedOnMode(
                        chaosModeEnabled,
                        MaterialTheme.colorScheme.surface,
                        ChaosKeyPink
                    ),
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            entry?.password?.let {
                                copyToClipboard(context, "Password", it)
                            }
                        }
                )

                // Visibility toggle button
                IconButton(
                    onClick = { isPasswordVisible = !isPasswordVisible },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password",
                        tint = ChaosKeyPink
                    )
                }
            }
        }

        // Show icon buttons only if the password has not been revealed
        if (!isPasswordFieldVisible) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(totalIcons) { index ->
                    IconButton(
                        onClick = {
                            selectedIconIndex = index
                            if (index == correctIconIndex) {
                                // Correct guess
                                isPasswordFieldVisible = true // Show the password field
                                onPasswordRevealed() // Callback for successful reveal
                            } else {
                                // Wrong guess
                                isPasswordFieldVisible = false // Keep the password field hidden
                            }
                        },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Guess Icon",
                            tint = if (index == correctIconIndex && selectedIconIndex == index)
                                ChaosAccept
                            else
                                ChaosKeyPink
                        )
                    }
                }
            }
        }
    }
}
