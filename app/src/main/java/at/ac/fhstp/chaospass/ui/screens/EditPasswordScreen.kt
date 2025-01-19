package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextFieldWithIcon
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.ChaosAccept
import at.ac.fhstp.chaospass.ui.theme.ChaosCancel
import at.ac.fhstp.chaospass.ui.theme.ChaosEdit
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.generateRandomPassword
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun EditPasswordScreen(
    entryId: Int,
    viewModel: EntryViewModel,
    navController: NavHostController,
    chaosModeEnabled: State<Boolean>
) {
    // Find the entry by ID
    val entry = viewModel.entries.collectAsState().value.find { it.id == entryId }

    // Mutable states for editable fields
    var siteName by remember { mutableStateOf(entry?.siteName ?: "") }
    var username by remember { mutableStateOf(entry?.username ?: "") }
    var password by remember { mutableStateOf(entry?.password ?: "") }
    val isPasswordVisible = remember { mutableStateOf(false) }
    val isSaveEnabled = remember(siteName, username, password) {
        siteName.isNotBlank() && username.isNotBlank() && password.isNotBlank()
    }

    // Chaos mode states
    var siteNameColor by remember { mutableStateOf(Color.Transparent) }
    var usernameColor by remember { mutableStateOf(Color.Transparent) }
    var passwordColor by remember { mutableStateOf(Color.Transparent) }
    var siteNameTranslationX by remember { mutableStateOf(0f) }
    var usernameRotation by remember { mutableStateOf(0f) }
    var passwordTranslationX by remember { mutableStateOf(0f) } // Horizontal movement
    var passwordRotation by remember { mutableStateOf(0f) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var countdownValue by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    val fieldsOrder = remember { mutableStateOf(listOf("Site Name", "Username", "Password")) }


    var dialogOffsetX by remember { mutableStateOf(0.dp) }
    var dialogOffsetY by remember { mutableStateOf(0.dp) }

    ScreenWrapper(
        onBackClick = { navController.popBackStack() },
        navController = navController,
        currentRoute = "edit/{entryId}",
        chaosModeEnabled = chaosModeEnabled
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),

            verticalArrangement = Arrangement.spacedBy(8.dp), // Add space between components
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            // Header Box
            HeaderBox(
                icon = Icons.Default.Edit,
                backgroundColor = getColorBasedOnMode(chaosModeEnabled.value, Color.White, ChaosEdit)
            )

            Spacer(modifier = Modifier.height(16.dp)) // Space between header and fields

            fieldsOrder.value.forEach { field ->
                when (field) {
                    "Site Name" -> {
                        CustomOutlinedTextField(
                            value = siteName,
                            onValueChange = { input ->
                                siteName = input
                                if (chaosModeEnabled.value) {
                                    siteNameTranslationX = (Random.nextInt(-20, 20)).toFloat()
                                    siteNameColor = Color(
                                        red = (0..255).random(),
                                        green = (0..255).random(),
                                        blue = (0..255).random()
                                    )
                                }
                            },
                            label = "Site Name",
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = siteNameTranslationX.dp)
                                .let { baseModifier ->
                                    if (chaosModeEnabled.value) {
                                        baseModifier.background(siteNameColor)
                                    } else baseModifier
                                }
                        )
                    }

                    "Username" -> {
                        CustomOutlinedTextField(
                            value = username,
                            onValueChange = { input ->
                                username = input
                                if (chaosModeEnabled.value) {
                                    usernameRotation = (Random.nextInt(-15, 15)).toFloat()
                                    usernameColor = Color(
                                        red = (0..255).random(),
                                        green = (0..255).random(),
                                        blue = (0..255).random()
                                    )
                                }
                            },
                            label = "Username",
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer(rotationZ = usernameRotation)
                                .let { baseModifier ->
                                    if (chaosModeEnabled.value) {
                                        baseModifier.background(usernameColor)
                                    } else baseModifier
                                }
                        )
                    }

                    "Password" -> {
                        CustomOutlinedTextFieldWithIcon(
                            value = password,
                            onValueChange = { input ->
                                password = input
                                if (chaosModeEnabled.value) {
                                    passwordTranslationX = (Random.nextInt(-20, 20)).toFloat()
                                    passwordRotation = (Random.nextInt(-15, 15)).toFloat()
                                    passwordColor = Color(
                                        red = (0..255).random(),
                                        green = (0..255).random(),
                                        blue = (0..255).random()
                                    )
                                }
                            },
                            label = "Password",
                            isPasswordField = true,
                            isPasswordVisible = isPasswordVisible.value,
                            onPasswordToggle = { isPasswordVisible.value = !isPasswordVisible.value },
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(x = passwordTranslationX.dp)
                                .graphicsLayer(rotationZ = passwordRotation)
                                .let { baseModifier ->
                                    if (chaosModeEnabled.value) {
                                        baseModifier.background(passwordColor)
                                    } else baseModifier
                                }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    val generatedPassword = generateRandomPassword()
                    password = generatedPassword
                    if (chaosModeEnabled.value) {
                        fieldsOrder.value = fieldsOrder.value.shuffled()
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(containerColor = KeyBlue)
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = "Generate Password",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Generate Password")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Space between buttons and other elements

            // Save and Cancel Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Save Button
                Button(
                    onClick = {
                        if (chaosModeEnabled.value) {
                            // Show looping save dialog in chaos mode
                            showSaveDialog = true
                        } else {
                            // Normal save functionality
                            viewModel.updateEntry(
                                Entry(
                                    id = entryId,
                                    siteName = siteName,
                                    username = username,
                                    password = password
                                )
                            )
                            navController.popBackStack()
                        }
                    },
                    enabled = isSaveEnabled,
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveEnabled) {
                            getColorBasedOnMode(
                                chaosModeEnabled.value,
                                AddGreen,
                                ChaosAccept
                            )
                        } else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Cancel Button
                Button(
                    onClick = {
                        if (chaosModeEnabled.value) {
                            // Random countdown dialog in chaos mode
                            countdownValue = (3..25).random()
                            showCancelDialog = true
                            coroutineScope.launch {
                                while (countdownValue > 0) {
                                    delay(1000)
                                    countdownValue--
                                }
                                showCancelDialog = false
                                navController.popBackStack()
                            }
                        } else {
                            // Normal cancel functionality
                            navController.popBackStack()
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColorBasedOnMode(chaosModeEnabled.value, MaterialTheme.colorScheme.error, ChaosCancel)
                    ),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Save Confirmation Dialog for Chaos Mode
        if (showSaveDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSaveDialog = false
                    // Reset dialog position on dismiss
                    dialogOffsetX = 0.dp
                    dialogOffsetY = 0.dp
                },
                title = { Text("Save Entry?") },
                text = { Text("Do you want to save this entry?") },
                modifier = Modifier.offset(x = dialogOffsetX, y = dialogOffsetY), // Apply dynamic offset
                confirmButton = {
                    TextButton(onClick = {
                        // Randomize dialog position to make it harder to click "Yes"
                        dialogOffsetX = (Random.nextInt(-50, 50)).dp
                        dialogOffsetY = (Random.nextInt(-50, 50)).dp
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        if (chaosModeEnabled.value) {
                            // Save the data in Chaos Mode when "No" is clicked
                            viewModel.updateEntry(
                                Entry(
                                    id = entryId,
                                    siteName = siteName,
                                    username = username,
                                    password = password
                                )
                            )
                        }
                        showSaveDialog = false // Dismiss the dialog
                        navController.popBackStack() // Navigate back
                    }) {
                        Text(if (chaosModeEnabled.value) "No! I would never!" else "No")
                    }
                }
            )
        }
        // Cancel Confirmation Dialog for Chaos Mode
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { showCancelDialog = false },
                title = { Text("Please wait...") },
                text = { Text("You can cancel in $countdownValue seconds.") },
                confirmButton = {
                    TextButton(onClick = {}) {
                        Text("I do not work right now. You have to Wait...")
                    }
                },
                dismissButton = null
            )
        }
    }
}
