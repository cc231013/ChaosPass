package at.ac.fhstp.chaospass.ui.screens


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
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
import at.ac.fhstp.chaospass.ui.theme.ChaosEdit
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnBlack
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.ShakeDetector
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
    val isLeftButtonPressed = remember { mutableStateOf(false) }
    val isRightButtonPressed = remember { mutableStateOf(false) }
    val isPasswordRevealed = isLeftButtonPressed.value && isRightButtonPressed.value

    val mockPasswords = listOf("wrong", "maybenexttime", "noooooo", "gotit Yet?", "not a chance")
    val allPasswords = remember(entry?.password) { mockPasswords + (entry?.password.orEmpty()) }
    var currentPasswordIndex by remember { mutableIntStateOf(0) }
    var currentPassword by remember { mutableStateOf(allPasswords.first()) }
    var isPasswordCorrect by remember { mutableStateOf(false) }
    var hintVisible by remember { mutableStateOf(false) } // Hint visibility state
    val context = LocalContext.current

    // Scenario Randomizer
    val chaosScenario = if (chaosModeEnabled.value) remember { (1..4).random() } else 0

    // Random fields count for Scenario 2
    val hiddenFields = remember { mutableStateListOf(*Array((10..100).random()) { it }) }
    val hiddenFieldMessage = remember { mutableStateOf("Click to reveal your information!") }

    // Shake detection logic (Scenario 3)
    val shakeDetector = remember {
        ShakeDetector(
            context = context,
            shakeThreshold = 12f,
            onShake = {
                // Cycle through passwords on shake
                currentPasswordIndex = (currentPasswordIndex + 1) % allPasswords.size
                currentPassword = allPasswords[currentPasswordIndex]
                isPasswordCorrect = currentPassword == entry?.password
            }
        )
    }

    DisposableEffect(chaosModeEnabled) {
        if (chaosModeEnabled.value && chaosScenario == 3) {
            shakeDetector.startListening()
        }

        onDispose {
            shakeDetector.stopListening()
        }
    }

    ScreenWrapper(
        onBackClick = { navController.popBackStack() },
        navController = navController,
        currentRoute = "details/{entryId}",
        chaosModeEnabled = chaosModeEnabled
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
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
                elevation = CardDefaults.cardElevation(8.dp),
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

                    // Chaos Mode: Apply scenarios
                    if (chaosModeEnabled.value) {
                        when (chaosScenario) {
                            1 -> {
                                // Scenario 1: Clickable hidden fields overlaying the username and password fields

                                Box(modifier = Modifier.fillMaxWidth()) {
                                    // Normal fields
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        InfoField(
                                            label = "Username",
                                            value = if (hiddenFields.isEmpty()) entry?.username.orEmpty() else "",
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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            InfoField(
                                                label = "Password",
                                                value = if (isPasswordVisible.value) entry?.password.orEmpty() else "••••••••",
                                                backgroundColor = getColorBasedOnMode(
                                                    chaosModeEnabled.value,
                                                    BackgroundBlue,
                                                    ChaosKeyPink
                                                ),
                                                labelColor = getColorBasedOnMode(
                                                    chaosModeEnabled.value,
                                                    BackgroundNavy,
                                                    ChaosOnColour
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )

                                            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                                                Icon(
                                                    imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                    contentDescription = if (isPasswordVisible.value) "Hide password" else "Show password",
                                                    tint = ChaosBackground
                                                )
                                            }
                                        }
                                    }

                                    // State to hold the random color for the button
                                    val buttonColor = remember { mutableStateOf(ChaosEdit) }

                                    // Overlaying hidden fields
                                    if (hiddenFields.isNotEmpty()) {
                                        Box(modifier = Modifier.matchParentSize()) {
                                            hiddenFields.forEachIndexed { index, _ ->
                                                Button(
                                                    onClick = {
                                                        hiddenFields.removeAt(index)

                                                        // Update the button color to a random color
                                                        buttonColor.value = Color(
                                                            red = Random.nextInt(0, 256),
                                                            green = Random.nextInt(0, 256),
                                                            blue = Random.nextInt(0, 256)
                                                        )

                                                        hiddenFieldMessage.value = when {
                                                            hiddenFields.isEmpty() -> "All fields cleared!"
                                                            hiddenFields.size == 1 -> "One more to go!"
                                                            else -> "Keep going, only ${hiddenFields.size} left!"
                                                        }
                                                    },
                                                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor.value),
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .fillMaxHeight(),
                                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                                                ) {
                                                    Text(text = "Click me")
                                                }
                                            }
                                        }
                                    }
                                }
                            }else -> {
                                // Scenario 2, 3, 4 handled below in the password section
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
                                    // Scenario 2: Dual hold to reveal
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        IconButton(
                                            onClick = { },
                                            modifier = Modifier
                                                .size(60.dp)
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onPress = {
                                                            isLeftButtonPressed.value = true
                                                            tryAwaitRelease()
                                                            isLeftButtonPressed.value = false
                                                        }
                                                    )
                                                }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Visibility,
                                                contentDescription = "Hold left to reveal",
                                                tint = ChaosBackground
                                            )
                                        }

                                        InfoField(
                                            label = "Password",
                                            value = if (isPasswordRevealed) entry?.password.orEmpty() else "••••••••",
                                            backgroundColor = getColorBasedOnMode(
                                                chaosModeEnabled.value,
                                                BackgroundBlue,
                                                ChaosKeyPink
                                            ),
                                            labelColor = getColorBasedOnMode(
                                                chaosModeEnabled.value,
                                                Color.Black,
                                                ChaosOnColour
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )

                                        IconButton(
                                            onClick = { },
                                            modifier = Modifier
                                                .size(60.dp)
                                                .pointerInput(Unit) {
                                                    detectTapGestures(
                                                        onPress = {
                                                            isRightButtonPressed.value = true
                                                            tryAwaitRelease()
                                                            isRightButtonPressed.value = false
                                                        }
                                                    )
                                                }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Visibility,
                                                contentDescription = "Hold right to reveal",
                                                tint = ChaosBackground
                                            )
                                        }
                                    }
                                }

                                3 -> {
                                    // Scenario 3: Password and icon appear randomly on the screen
                                    val handler = android.os.Handler(android.os.Looper.getMainLooper())
                                    var timerStarted by remember { mutableStateOf(false) }

                                    Box(
                                        modifier = Modifier
                                            .height(120.dp)
                                            .fillMaxWidth()


                                    ) {
                                        if (isPasswordVisible.value) {
                                            Text(
                                                text = currentPassword,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = Color((0..255).random(), (0..255).random(), (0..255).random()),
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .padding(
                                                        start = (10..300).random().dp,
                                                        top = (10..120).random().dp
                                                    )
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                isPasswordVisible.value = true
                                                currentPassword = entry?.password.orEmpty()

                                                if (!timerStarted) {
                                                    timerStarted = true
                                                    handler.postDelayed({
                                                        isPasswordVisible.value = false
                                                        timerStarted = false
                                                    }, 5000) // Hide after 5 seconds
                                                }
                                            },
                                            modifier = Modifier
                                                .align(Alignment.Center)
                                                .padding(
                                                    start = (10..300).random().dp,
                                                    top = (10..120).random().dp
                                                )
                                        ) {
                                            Icon(
                                                imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = "Toggle password visibility",
                                                tint = ChaosKeyPink
                                            )
                                        }
                                    }
                                }

                                4-> {
                                    // Scenario 4: Shake detection or default behavior
                                    InfoField(
                                        label = "Password",
                                        value = currentPassword,
                                        backgroundColor = if (chaosScenario == 3) {
                                            if (isPasswordCorrect) ChaosAccept else ChaosCancel
                                        } else getColorBasedOnMode(chaosModeEnabled.value, BackgroundBlue, ChaosEdit),
                                        labelColor = BackgroundNavy,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                            }
                        } else {
                            // Normal Mode: Default visibility toggle
                            InfoField(
                                label = "Password",
                                value = if (isPasswordVisible.value) entry?.password.orEmpty() else "••••••••",
                                backgroundColor = getColorBasedOnMode(chaosModeEnabled.value, BackgroundBlue, ChaosKeyPink),
                                labelColor = BackgroundNavy,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                                Icon(
                                    imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle password visibility",
                                    tint = ChaosBackground
                                )
                            }
                        }
                    }
                }
            }

            // Hint Icon and Hint Text
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
                                2 -> "Hold both icons to reveal the password!"
                                3 -> "Click the toggle to make the password appear!"
                                4 -> "Shake your phone to find your password!"
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
                    onClick = { navController.navigate("edit/$entryId") },
                    shape = MaterialTheme.shapes.large,
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

                Button(
                    onClick = { showConfirmationDialog = true },
                    shape = MaterialTheme.shapes.large,
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
            }

            if (showConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmationDialog = false },
                    title = { Text("Delete Entry?") },
                    text = {
                        Text(
                            if (chaosModeEnabled.value) {
                                "Deleting is not possible in Chaos Mode. Are you sure?"
                            } else {
                                "Are you sure you want to delete this entry?"
                            }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (chaosModeEnabled.value) {
                                // Reopen the same dialog for Chaos Mode
                                showConfirmationDialog = true
                            } else {
                                // Delete the entry in normal mode
                                entry?.let {
                                    viewModel.deleteEntry(it)
                                    navController.popBackStack()
                                }
                                showConfirmationDialog = false
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            if (chaosModeEnabled.value) {
                                // Open the "Good choice" dialog in Chaos Mode
                                showConfirmationDialog = false
                                showGoodChoiceDialog = true
                            } else {
                                showConfirmationDialog = false
                            }
                        }) {
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