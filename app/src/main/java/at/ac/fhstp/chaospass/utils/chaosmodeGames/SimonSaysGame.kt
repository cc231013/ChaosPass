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
    var showWrongMessage by remember { mutableStateOf(false) }

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoField(
                        label = "Password",
                        value = entry?.password.orEmpty(),
                        backgroundColor = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            MaterialTheme.colorScheme.surface,
                            ChaosKeyPink
                        ),
                        isPasswordField = true,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                            .clickable {
                                entry?.password?.let {
                                    copyToClipboard(context, "Password", it)
                                }
                            }
                    )
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
                }

                // Fixed height for the sequence area to prevent layout shifting
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isSequenceVisible) {
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
                    } else {
                        Text(
                            text = "Input the sequence below:",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Display player's sequence
                if (playerSequence.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
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

                // Combined Submit and Restart Button
                Button(
                    onClick = {
                        if (!showWrongMessage) {
                            if (playerSequence.size == currentSequence.size &&
                                playerSequence.zip(currentSequence).all { (player, target) -> player == target }
                            ) {
                                sequenceGameComplete = true
                                onComplete()
                            } else {
                                showWrongMessage = true
                            }
                        } else {
                            // Reset the game
                            playerSequence.clear()
                            currentSequence = List(4) { colorList.random() }
                            showWrongMessage = false
                            Log.d("SimonSaysGame", "New Sequence Generated: $currentSequence")
                        }
                    },
                    enabled = playerSequence.size == currentSequence.size || showWrongMessage,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text(if (showWrongMessage) "Restart" else "Submit")
                }

                if (showWrongMessage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Wrong Sequence! Try Again.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ChaosCancel
                    )
                }
            }
        }
    }
}
