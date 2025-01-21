package at.ac.fhstp.chaospass.utils.chaosmodeGames

import android.content.Context
import android.os.Handler
import android.os.Looper
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour
import at.ac.fhstp.chaospass.utils.copyToClipboard
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode

@Composable
fun TypeRace(
    entry: Entry?,
    onComplete: () -> Unit,
    chaosModeEnabled: Boolean,
    context: Context
) {
    val randomStringLength = 7
    val randomString = remember { mutableStateOf(generateRandomString(randomStringLength)) }
    val playerInput = remember { mutableStateOf("") }
    var xOffset by remember { mutableStateOf(0.dp) }
    val speed = if (chaosModeEnabled) 5.dp else 3.dp
    val timerRunning = remember { mutableStateOf(true) }
    val isChallengeCompleted = remember { mutableStateOf(false) }

    val handler = remember { Handler(Looper.getMainLooper()) }
    DisposableEffect(Unit) {
        val runnable = object : Runnable {
            override fun run() {
                if (timerRunning.value) {
                    xOffset += speed
                    if (xOffset > 263.dp) { // Reset if string moves off-screen
                        xOffset = 0.dp
                        randomString.value = generateRandomString(randomStringLength)
                        playerInput.value = "" // Clear the input field
                    }
                    handler.postDelayed(this, 150L)
                }
            }
        }
        handler.post(runnable)
        onDispose {
            handler.removeCallbacksAndMessages(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChaosAddBlue),

    ) {
        if (!isChallengeCompleted.value) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Type the Moving String",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Moving String
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = getColorBasedOnMode(
                            chaosModeEnabled,
                            MaterialTheme.colorScheme.surface,
                            ChaosKeyPink
                        )
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(
                        text = randomString.value,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.offset(x = xOffset)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))


                CustomOutlinedTextField(
                    value = playerInput.value,
                    onValueChange = { input ->
                        playerInput.value = input
                        if (input == randomString.value) {
                            timerRunning.value = false
                            isChallengeCompleted.value = true
                            onComplete()
                        }
                    },
                    label = "Type here...",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            getColorBasedOnMode(
                                chaosModeEnabled,
                                BackgroundBlue,
                                ChaosAddBlue
                            )
                        )
                )
            }
        } else {
            // Show InfoField content after the challenge is complete
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoField(
                    label = "Username",
                    value = entry?.username.orEmpty(),
                    backgroundColor = getColorBasedOnMode(
                        chaosModeEnabled,
                        BackgroundBlue,
                        ChaosKeyPink
                    ),
                    labelColor = getColorBasedOnMode(
                        chaosModeEnabled,
                        BackgroundNavy,
                        ChaosOnColour
                    )
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    InfoField(
                        label = "Password",
                        value = entry?.password.orEmpty(),
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        isPasswordField = true,
                        modifier = Modifier.weight(1f)
                            .clickable {
                                entry?.password?.let {
                                    copyToClipboard(context, "Password", it)
                                }
                            }
                    )

                }
            }
        }
    }
}

// Utility function to generate a random string
fun generateRandomString(length: Int): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length).map { chars.random() }.joinToString("")
}
