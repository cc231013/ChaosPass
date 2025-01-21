package at.ac.fhstp.chaospass.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.ChaosAccept
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue
import at.ac.fhstp.chaospass.ui.theme.ChaosBackground
import at.ac.fhstp.chaospass.ui.theme.ChaosCancel
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnBlack
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.chaosmodeGames.CatchMeIfYouCan
import at.ac.fhstp.chaospass.utils.chaosmodeGames.FieldClicker
import at.ac.fhstp.chaospass.utils.chaosmodeGames.IconGuess
import at.ac.fhstp.chaospass.utils.chaosmodeGames.SimonSaysGame
import at.ac.fhstp.chaospass.utils.chaosmodeGames.TriviaGame
import at.ac.fhstp.chaospass.utils.chaosmodeGames.TypeRace
import at.ac.fhstp.chaospass.utils.copyToClipboard
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel
import kotlin.random.Random


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun PasswordDetailScreen(
    entryId: Int,
    viewModel: EntryViewModel,
    navController: NavHostController,
    chaosModeEnabled: State<Boolean>
) {
    val entry = viewModel.entries.collectAsState().value.find { it.id == entryId }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val context = LocalContext.current

    var hintVisible by remember { mutableStateOf(false) }
    val chaosScenario = if (chaosModeEnabled.value) remember { (1..6).random() } else 0
    val hiddenFields = remember { mutableStateListOf(*Array((10..100).random()) { it }) }
    val hiddenFieldMessage = remember { mutableStateOf("Click to reveal your information!") }
    var dialogOffsetX by remember { mutableStateOf(0.dp) }
    var dialogOffsetY by remember { mutableStateOf(0.dp) }


    ScreenWrapper(
        onBackClick = {
            navController.navigate("list") {
                popUpTo("list") { inclusive = false }
            }
        },
        navController = navController,
        currentRoute = "details/{entryId}",
        chaosModeEnabled = chaosModeEnabled
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top= 16.dp, start = 16.dp, end = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBox(
                icon = Icons.Default.Key,
                rotation = 135f,
                backgroundColor = getColorBasedOnMode(
                    chaosModeEnabled.value,
                    KeyBlue,
                    ChaosKeyPink
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = getColorBasedOnMode(
                        chaosModeEnabled.value,
                        MaterialTheme.colorScheme.surface,
                        ChaosAddBlue
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = entry?.siteName.orEmpty(),
                        color = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            MaterialTheme.colorScheme.onSurface,
                            ChaosOnColour
                        ),
                        style = MaterialTheme.typography.titleLarge
                    )

                    if (chaosModeEnabled.value) {
                        when (chaosScenario) {
                            1 -> {
                                FieldClicker(entry, chaosModeEnabled.value, hiddenFields, hiddenFieldMessage)
                            }else -> {

                                InfoField(
                                    label = "Username",
                                    value = entry?.username.orEmpty(),
                                    backgroundColor = getColorBasedOnMode(
                                        chaosModeEnabled.value,
                                        BackgroundBlue,
                                        ChaosKeyPink
                                    ),
                                    labelColor = getColorBasedOnMode(
                                        chaosModeEnabled.value,
                                        BackgroundNavy,
                                        ChaosOnColour
                                    )
                                )
                            }
                        }
                    } else {
                        // Normal Mode: Show username without any scenario
                        InfoField(
                            label = "Username",
                            value = entry?.username.orEmpty(),
                            backgroundColor = BackgroundBlue,
                            labelColor = BackgroundNavy
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (chaosModeEnabled.value) {
                            when (chaosScenario) {
                                2 -> {
                                    // Scenario 2: Find the right Icon
                                        IconGuess(
                                            entry, chaosModeEnabled.value
                                        ) {
                                            isPasswordVisible.value = true
                                }
                                }

                                3 -> {
                                    //Scenario 3: Moving Password
                                    CatchMeIfYouCan(
                                        currentPassword = entry?.password.orEmpty(),
                                        onComplete = {}
                                    )
                                }
                                4 -> {
                                    if (entry != null) {
                                        TriviaGame(
                                            entry = entry,
                                            password = entry.password,
                                            onCorrectAnswer = { /* Handle correct answer logic */ },
                                            chaosModeEnabled = chaosModeEnabled.value, // Pass chaosModeEnabled here
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                                5 -> {
                                    //Scenario 5: Colour Sequences
                                    SimonSaysGame(
                                        entry = entry,
                                        chaosModeEnabled = chaosModeEnabled,
                                        onComplete = { /* Handle completion */ },
                                        context = LocalContext.current
                                    )
                                }
                                6 -> {
                                    // Scenario 6: Type the moving String
                                    TypeRace(
                                        entry = entry,
                                        chaosModeEnabled = chaosModeEnabled.value,
                                        onComplete = {
                                            isPasswordVisible.value = true
                                        },
                                        context = context
                                    )
                                }

                            }
                        } else {
                            // Normal Mode: Default visibility toggle
                            InfoField(
                                label = "Password",
                                value = entry?.password.orEmpty() ,
                                backgroundColor = getColorBasedOnMode(chaosModeEnabled.value, BackgroundBlue, ChaosKeyPink),
                                labelColor = BackgroundNavy,
                                isPasswordField = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            if (chaosModeEnabled.value) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = { hintVisible = !hintVisible }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = "Hint",
                            tint = getColorBasedOnMode(chaosModeEnabled.value, Color.Black, ChaosKeyPink),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    if (hintVisible) {
                        Text(
                            text = when (chaosScenario) {
                                1 -> hiddenFieldMessage.value
                                2 -> "Oh no! Which one is it?"
                                3 -> "Catch me if you can!"
                                4 -> "Are you smart enough?"
                                5 -> "Michail says: Click the Buttons!"
                                6 -> "Fast! Enter the String!"
                                else -> "Good luck!"
                            },
                            color = getColorBasedOnMode(
                                chaosModeEnabled.value,
                                MaterialTheme.colorScheme.onSurface,
                                ChaosOnBlack
                            ),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }

            // Buttons
            var showConfirmationDialog by remember { mutableStateOf(false) }
            var showGoodChoiceDialog by remember { mutableStateOf(false) }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { showConfirmationDialog = true },
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            MaterialTheme.colorScheme.error,
                            ChaosCancel
                        )
                    ),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(32.dp)
                    )
                }
                Button(
                    onClick = { navController.navigate("edit/$entryId") },
                    shape = MaterialTheme.shapes.large,
                    elevation = ButtonDefaults.buttonElevation(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            AddGreen,
                            ChaosKeyPink
                        )
                    ),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.Black,
                        modifier = Modifier.size(32.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (showConfirmationDialog) {
                var yesClickCount by remember { mutableStateOf(0) }

                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    modifier = Modifier.offset(x = dialogOffsetX, y = dialogOffsetY),
                    title = { Text("Delete Entry?") },
                    text = {
                        Text(
                            if (chaosModeEnabled.value) {
                                when {
                                    yesClickCount >= 10 -> "No more clicks! Try a different button!"
                                    yesClickCount >= 5 -> " Maybe try a different button?"
                                    else -> "Are you sure? It seems like a bad idea! Think again!"
                                }
                            } else {
                                "Are you sure you want to delete this entry?"
                            }
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                if (chaosModeEnabled.value) {
                                    if (yesClickCount < 10) {
                                        yesClickCount++
                                        dialogOffsetX = (Random.nextInt(-20, 20)).dp
                                        dialogOffsetY = (Random.nextInt(-20, 20)).dp
                                    }
                                } else {
                                    entry?.let {
                                        viewModel.deleteEntry(it)
                                        navController.popBackStack()
                                    }
                                    showConfirmationDialog = false
                                }
                            },
                            enabled = yesClickCount < 10
                        ) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                if (chaosModeEnabled.value) {
                                    showConfirmationDialog = false
                                    showGoodChoiceDialog = true
                                } else {
                                    showConfirmationDialog = false
                                }
                            },
                            colors = if (chaosModeEnabled.value && yesClickCount >= 10) {
                                ButtonDefaults.textButtonColors(containerColor = KeyBlue)
                            } else {
                                ButtonDefaults.textButtonColors()
                            }
                        ) {
                            Text("No")
                        }
                    }
                )
            }
            if (showGoodChoiceDialog) {
                AlertDialog(
                    onDismissRequest = { showGoodChoiceDialog = false },
                    title = { Text("Good Choice!") },
                    text = { Text("Good choice, since deleting is not possible in Chaos Mode.") },
                    confirmButton = {
                        TextButton(onClick = { showGoodChoiceDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}