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
        "What is the derivative of x^2?" to "2x",
        "Who painted the Sistine Chapel ceiling?" to "michelangelo",
        "What is the capital of Australia?" to "canberra",
        "What element has the atomic number 79?" to "gold",
        "What is the smallest prime number?" to "2",
        "What year did the Titanic sink?" to "1912",
        "What is the longest river in the world?" to "nile",
        "In what year was the first iPhone released?" to "2007",
        "What is the term for a word spelled the same forward and backward?" to "palindrome",
        "What is the sum of the angles in a triangle?" to "180",
        "What is the hexadecimal value of the number 255?" to "ff",
        "What is the name of the AI in '2001: A Space Odyssey'?" to "hal",
        "What is the binary representation of the decimal number 10?" to "1010",
        "What does HTTP stand for?" to "hypertext transfer protocol",
        "Who directed the movie 'The Matrix'?" to "wachowskis",
        "What is the name of the computer in 'The Hitchhiker's Guide to the Galaxy' that calculated 42?" to "deep thought",
        "What is the first element on the periodic table?" to "hydrogen",
        "What is the capital city of Iceland?" to "reykjavik",
        "Who won the first Nobel Prize in Physics?" to "roentgen",
        "What is the term for a star's explosive death?" to "supernova",
        "What language has the most native speakers?" to "mandarin",
        "What year did the Berlin Wall fall?" to "1989",
        "What is 2+2?" to "4",
        "How many little pigs where there?" to "3",
        "How many spices are in fivespice?" to "5",
        "How many letters are in phones?" to "6",
        "why was 8 afraid of 7?" to "789",
        "What is the currency of Japan?" to "yen",
        "Who is the author of 'A Brief History of Time'?" to "stephen hawking",
        "What was the name of the first man-made satellite launched into space?" to "sputnik",
        "What is the square root of 256?" to "16",
        "What is the largest planet in the Solar System?" to "jupiter"
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
            keyboardActions = KeyboardActions(onDone = {}),
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
                onCorrectAnswer()
            } else {
                feedbackMessage = "Wrong! Try again."
                currentQuestionIndex = Random.nextInt(triviaQuestions.size) // Pick a new question
                answer = ""
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
