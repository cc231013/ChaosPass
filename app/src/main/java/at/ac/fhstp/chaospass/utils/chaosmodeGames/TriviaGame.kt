package at.ac.fhstp.chaospass.utils.chaosmodeGames

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.utils.copyToClipboard
import kotlin.random.Random

@Composable
fun TriviaGame(
    password: String,
    entry: Entry,
    onCorrectAnswer: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Pool of trivia questions
    val triviaQuestions = listOf(
        "What is 2 + 2?" to "4",
        "What is the capital of France?" to "paris",
        "Spell 'password'" to "password",
        "What color is the sky?" to "blue",
        "What is 5 * 3?" to "15",
        "What planet is known as the Red Planet?" to "mars",
        "Who wrote 'Hamlet'?" to "shakespeare",
        "What is the largest ocean on Earth?" to "pacific",
        "What is the square root of 16?" to "4",
        "How many days are in a leap year?" to "366"
    )

    var currentQuestionIndex by remember { mutableStateOf(Random.nextInt(triviaQuestions.size)) }
    var answer by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Display current question
        Text(
            text = triviaQuestions[currentQuestionIndex].first,
            style = androidx.compose.material3.MaterialTheme.typography.bodyLarge
        )

        // Answer input field
        OutlinedTextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Your Answer") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(onDone = { /* Handle keyboard dismissal */ }),
            modifier = Modifier.fillMaxWidth()
        )

        // Feedback for correct/incorrect answer
        if (feedbackMessage.isNotEmpty()) {
            Text(text = feedbackMessage)
        }

        // Submit button
        Button(onClick = {
            if (answer.equals(triviaQuestions[currentQuestionIndex].second, ignoreCase = true)) {
                feedbackMessage = "Correct!"
                onCorrectAnswer() // Trigger onCorrectAnswer callback
            } else {
                feedbackMessage = "Wrong! Try a new question."
                currentQuestionIndex = Random.nextInt(triviaQuestions.size) // Pick a new question
                answer = "" // Clear the answer field
            }
        }) {
            Text("Submit")
        }

        // Display the password field with visibility toggle
        if (feedbackMessage.startsWith("Correct!")) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // InfoField for the password
                InfoField(
                    label = "Password",
                    value = if (isPasswordVisible) password else "••••••••",
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                                        .clickable {
                                            copyToClipboard(context, "Password", entry.password)
                                        }
                )

                // Visibility toggle button
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password"
                    )
                }
            }
        }
    }
}
