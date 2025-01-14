package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextFieldWithIcon
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.generateRandomPassword
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPasswordScreen(
    entryId: Int,
    viewModel: EntryViewModel,
    navController: NavHostController
) {
    // Find the entry by ID
    val entry = viewModel.entries.collectAsState().value.find { it.id == entryId }

    // Mutable states for editable fields
    var siteName by remember { mutableStateOf(entry?.siteName ?: "") }
    var username by remember { mutableStateOf(entry?.username ?: "") }
    var password by remember { mutableStateOf(entry?.password ?: "") }
    val isPasswordVisible = remember { mutableStateOf(false) }

    ScreenWrapper(
        navController = navController,
        currentRoute = "edit/{entryId}"
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Box
            HeaderBox(icon = Icons.Default.Edit, backgroundColor = Color.White)

            if (entry != null) {
                CustomOutlinedTextField(
                    value = siteName,
                    onValueChange = { siteName = it },
                    label = "Site Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomOutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "Username",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                CustomOutlinedTextFieldWithIcon(
                    value = password,
                    onValueChange = { password = it },
                    label = "Password",
                    isPasswordField = true,
                    isPasswordVisible = isPasswordVisible.value,
                    onPasswordToggle = { isPasswordVisible.value = !isPasswordVisible.value },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Generate Password Button
                Button(
                    onClick = {
                        password= generateRandomPassword()
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

                // Save and Cancel Buttons
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.updateEntry(
                                Entry(
                                    id = entryId,
                                    siteName = siteName,
                                    username = username,
                                    password = password
                                )
                            )
                            navController.popBackStack() // Navigate back after saving
                        },
                        enabled = siteName.isNotBlank() && username.isNotBlank() && password.isNotBlank(),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(containerColor = AddGreen),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = "Save",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    // Cancel Button with Close Icon
                    Button(
                        onClick = { navController.popBackStack() },
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
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
            } else {
                Text("Loading entry details...", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
