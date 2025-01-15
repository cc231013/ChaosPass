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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextFieldWithIcon
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.ChaosAccept
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue
import at.ac.fhstp.chaospass.ui.theme.ChaosCancel
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.generateRandomPassword
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun AddPasswordScreen(
    viewModel: EntryViewModel,
    navController: NavHostController,
    chaosModeEnabled: State<Boolean>
) {
    val siteName = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var siteNameColor by remember { mutableStateOf(Color.Transparent) }
    var usernameColor by remember { mutableStateOf(Color.Transparent) }
    var passwordColor by remember { mutableStateOf(Color.Transparent) }
    var siteNameTranslationX by remember { mutableStateOf(0f) }
    var usernameRotation by remember { mutableStateOf(0f) }
    var passwordTranslationX by remember { mutableStateOf(0f) } // Horizontal movement
    var passwordRotation by remember { mutableStateOf(0f) } // Rotation
    val isPasswordVisible = remember { mutableStateOf(false) }
    var showSaveConfirmationDialog by remember { mutableStateOf(false) }
    var showCancelConfirmationDialog by remember { mutableStateOf(false) }
    var cancelDialogCounter by remember { mutableStateOf(0) }
    val randomCancelLimit = remember { Random.nextInt(1, 10) }

    var saveDialogOffsetX by remember { mutableStateOf(0.dp) }
    var saveDialogOffsetY by remember { mutableStateOf(0.dp) }
    var cancelDialogOffsetX by remember { mutableStateOf(0.dp) }
    var cancelDialogOffsetY by remember { mutableStateOf(0.dp) }

    // Check if all fields are filled
    val isSaveEnabled = remember(siteName.value, username.value, password.value) {
        siteName.value.isNotBlank() && username.value.isNotBlank() && password.value.isNotBlank()
    }

    ScreenWrapper(
        onBackClick = { navController.popBackStack() },
        navController = navController,
        currentRoute = "add",
        chaosModeEnabled = chaosModeEnabled

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBox(
                icon = Icons.Default.Add,
                backgroundColor = getColorBasedOnMode(
                    chaosModeEnabled.value,
                    AddGreen,
                    ChaosAddBlue
                )
            )

            Text(text = "Add New Password", style = MaterialTheme.typography.titleLarge)

            CustomOutlinedTextField(
                value = siteName.value,
                onValueChange = { input ->
                    siteName.value = input

                    if (chaosModeEnabled.value) {
                        // Trigger horizontal movement
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
                    .offset(x = siteNameTranslationX.dp) // Apply horizontal movement
                    .let { baseModifier ->
                        if (chaosModeEnabled.value) {
                            baseModifier.background(siteNameColor)
                        } else baseModifier
                    }
            )

            CustomOutlinedTextField(
                value = username.value,
                onValueChange = { input ->
                    username.value = input

                    if (chaosModeEnabled.value) {
                        // Trigger rotation
                        usernameRotation = (Random.nextInt(
                            -15,
                            15
                        )).toFloat() // Random rotation between -15° and 15°
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
                    .graphicsLayer(rotationZ = usernameRotation) // Apply rotation
                    .let { baseModifier ->
                        if (chaosModeEnabled.value) {
                            baseModifier.background(usernameColor)
                        } else baseModifier
                    }
            )

            CustomOutlinedTextFieldWithIcon(
                value = password.value,
                onValueChange = { input ->
                    password.value = input
                    if (chaosModeEnabled.value) {
                        // Trigger random horizontal movement
                        passwordTranslationX = (Random.nextInt(-20, 20)).toFloat()

                        // Trigger random rotation
                        passwordRotation =
                            (Random.nextInt(-15, 15)).toFloat() // Rotation between -15° and 15°

                        // Change background color randomly
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
                    .offset(x = passwordTranslationX.dp) // Apply horizontal movement
                    .graphicsLayer(rotationZ = passwordRotation) // Apply rotation
                    .let { baseModifier ->
                        if (chaosModeEnabled.value) {
                            baseModifier.background(passwordColor) // Apply color change
                        } else baseModifier
                    }
            )
            Button(
                onClick = {
                    val generatedPassword = generateRandomPassword()
                    password.value = generatedPassword
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

            Spacer(modifier = Modifier.height(16.dp))



            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Cancel Button with Icon
                Button(
                    onClick = {
                        if (chaosModeEnabled.value) {
                            showCancelConfirmationDialog = true
                            cancelDialogCounter = 0 // Reset the counter in chaos mode
                        } else {
                            navController.popBackStack() // Navigate back in normal mode
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = getColorBasedOnMode(
                            chaosModeEnabled.value,
                            MaterialTheme.colorScheme.error,
                            ChaosCancel
                        )
                    ),
                    modifier = Modifier.size(80.dp) // Circular button size
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Save Button with Icon
                Button(
                    onClick = {
                        if (chaosModeEnabled.value) {
                            showSaveConfirmationDialog = true
                        } else {
                            viewModel.addEntry(
                                siteName = siteName.value.trim(),
                                username = username.value.trim(),
                                password = password.value.trim()
                            )
                            navController.popBackStack() // Navigate back
                        }
                    },
                    enabled = isSaveEnabled, // Enable or disable based on input validation
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveEnabled) {
                            getColorBasedOnMode(
                                chaosModeEnabled.value,
                                AddGreen,
                                ChaosAccept
                            )
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Disabled color
                        }
                    ),
                    modifier = Modifier.size(80.dp) // Circular button size
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = if (isSaveEnabled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // Save Confirmation Dialog
            if (showSaveConfirmationDialog) {

                AlertDialog(
                    onDismissRequest = {
                        showSaveConfirmationDialog = false
                        if (chaosModeEnabled.value) {
                            saveDialogOffsetX = 0.dp
                            saveDialogOffsetY = 0.dp
                        }
                    },
                    title = { Text("Save Entry?") },
                    text = { Text("Do you want to save this entry?") },
                    modifier = if (chaosModeEnabled.value) {
                        Modifier.offset(x = saveDialogOffsetX, y = saveDialogOffsetY)
                    } else Modifier,
                    confirmButton = {
                        TextButton(onClick = {
                            if (chaosModeEnabled.value) {
                                // Move the dialog slightly in chaos mode
                                saveDialogOffsetX = (Random.nextInt(-20, 20)).dp
                                saveDialogOffsetY = (Random.nextInt(-20, 20)).dp
                            } else {
                                // Shouldn't happen as dialog is not shown in normal mode
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            // Save and navigate back in chaos mode when "No" is clicked
                            viewModel.addEntry(
                                siteName = siteName.value.trim(),
                                username = username.value.trim(),
                                password = password.value.trim()
                            )
                            navController.popBackStack()
                            showSaveConfirmationDialog = false
                        }) {
                            Text("No")
                        }
                    }
                )
            }

            // Cancel Confirmation Dialog
            if (showCancelConfirmationDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showCancelConfirmationDialog = false
                        if (chaosModeEnabled.value) {
                            cancelDialogOffsetX = 0.dp
                            cancelDialogOffsetY = 0.dp
                        }
                    },
                    title = { Text("Are you sure?") },
                    text = { Text("Do you really want to cancel?") },
                    modifier = if (chaosModeEnabled.value) {
                        Modifier.offset(x = cancelDialogOffsetX, y = cancelDialogOffsetY)
                    } else Modifier,
                    confirmButton = {
                        TextButton(onClick = {
                            if (chaosModeEnabled.value) {
                                // Reopen the dialog with a random offset in chaos mode
                                cancelDialogCounter += 1
                                if (cancelDialogCounter < randomCancelLimit) {
                                    cancelDialogOffsetX = (Random.nextInt(-20, 20)).dp
                                    cancelDialogOffsetY = (Random.nextInt(-20, 20)).dp
                                    showCancelConfirmationDialog = true
                                } else {
                                    showCancelConfirmationDialog = false
                                    navController.popBackStack()
                                }
                            } else {
                                // Navigate back in normal mode
                                showCancelConfirmationDialog = false
                                navController.popBackStack()
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showCancelConfirmationDialog = false
                        }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

