package at.ac.fhstp.chaospass.utils.chaosmodeGames

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.ChaosCancel
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour
import at.ac.fhstp.chaospass.utils.copyToClipboard
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode

@Composable
fun SimonSaysGame(
    entry: Entry?,
    chaosModeEnabled: State<Boolean>,
    onComplete: () -> Unit,
    context: Context
) {
    val colorList = listOf(
        Color.Green, Color.Yellow, Color.Red, Color.Blue,
        Color(0xFF750CB6), // Purple
        Color(0xFFEF63E1)  // Pink
    )

    var currentSequence by remember { mutableStateOf(List(4) { colorList.random() }) }
    val playerSequence = remember { mutableStateListOf<Color>() }
    var sequenceGameComplete by remember { mutableStateOf(false) }
    var isSequenceVisible by remember { mutableStateOf(true) }
    var showRestartButton by remember { mutableStateOf(false) }

    val handler = remember { Handler(Looper.getMainLooper()) }

    // Log the initial sequence
    Log.d("SimonSaysGame", "Generated Sequence: $currentSequence")

    // Show the sequence briefly at the start
    DisposableEffect(currentSequence) {
        isSequenceVisible = true
        handler.postDelayed({ isSequenceVisible = false }, 2000L) // Show for 2 seconds

        onDispose {
            handler.removeCallbacksAndMessages(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        if (sequenceGameComplete) {
            // Display the password field after successful completion
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {

                // Password field with visibility toggle
                var isPasswordVisible by remember { mutableStateOf(false) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoField(
                        label = "Password",
                        value = if (isPasswordVisible) entry?.password.orEmpty() else "••••••••",
                        backgroundColor = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            MaterialTheme.colorScheme.surface,
                            ChaosKeyPink
                        ),
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
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
        } else {
            // Main game view
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.Transparent)
            ) {
                if (isSequenceVisible) {
                    Text(
                        text = "Memorize the sequence:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.wrapContentSize()
                    ) {
                        currentSequence.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(color, shape = MaterialTheme.shapes.small)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Michail says:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Display player's sequence
                if (playerSequence.isNotEmpty()) {
                    Text(
                        text = "Your Sequence:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .wrapContentSize()
                    ) {
                        playerSequence.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(30.dp)
                                    .background(color, shape = MaterialTheme.shapes.small)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Buttons for input
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    colorList.chunked(3).forEach { rowColors ->
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            rowColors.forEach { color ->
                                Button(
                                    onClick = {
                                        if (playerSequence.size < currentSequence.size) {
                                            playerSequence.add(color)
                                            Log.d("SimonSaysGame", "Player Input: $playerSequence")
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = color,
                                        contentColor = Color.Black
                                    ),
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(2.dp)
                                ) {}
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Submit button
                Button(
                    onClick = {
                        Log.d("SimonSaysGame", "Submit Pressed")
                        Log.d("SimonSaysGame", "Generated Sequence: $currentSequence")
                        Log.d("SimonSaysGame", "Player Sequence: $playerSequence")
                        if (playerSequence.size == currentSequence.size &&
                            playerSequence.zip(currentSequence).all { (player, target) -> player == target }
                        ) {
                            sequenceGameComplete = true
                            onComplete()
                        } else {
                            showRestartButton = true
                        }
                    },
                    enabled = playerSequence.size == currentSequence.size,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Submit")
                }

                // Restart option on failure
                if (showRestartButton) {
                    Text(
                        text = "Wrong Sequence! Try Again.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ChaosCancel
                    )
                    Button(onClick = {
                        playerSequence.clear()
                        showRestartButton = false
                        currentSequence = List(4) { colorList.random() }
                        Log.d("SimonSaysGame", "New Sequence Generated: $currentSequence")
                    }) {
                        Text("Restart")
                    }
                }
            }
        }
    }
}
