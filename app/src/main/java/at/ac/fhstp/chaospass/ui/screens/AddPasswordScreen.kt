package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lint.kotlin.metadata.Visibility
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.ui.components.BottomNavigationBar
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextFieldWithIcon
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.generateRandomPassword
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

@Composable
fun AddPasswordScreen(
    viewModel: EntryViewModel,
    navController: NavHostController,
    onCancel: () -> Unit
) {
    val siteName = remember { mutableStateOf("") }
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val isPasswordVisible = remember { mutableStateOf(false) }

    ScreenWrapper(
        navController = navController,
        currentRoute = "add"

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBox(icon = Icons.Default.Add, backgroundColor = AddGreen)

            Text(text = "Add New Password", style = MaterialTheme.typography.titleLarge)

            CustomOutlinedTextField(
                value = siteName.value,
                onValueChange = { siteName.value = it },
                label = "Site Name",
                modifier = Modifier.fillMaxWidth()
            )

            CustomOutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = "Username",
                modifier = Modifier.fillMaxWidth()
            )

            CustomOutlinedTextFieldWithIcon(
                value = password.value,
                onValueChange = { password.value = it },
                label = "Password",
                isPasswordField = true,
                isPasswordVisible = isPasswordVisible.value,
                onPasswordToggle = { isPasswordVisible.value = !isPasswordVisible.value },
                modifier = Modifier.fillMaxWidth()
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
                    onClick = onCancel,
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
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
                        viewModel.addEntry(
                            siteName = siteName.value.trim(),
                            username = username.value.trim(),
                            password = password.value.trim()
                        )
                        onCancel()
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = AddGreen),
                    modifier = Modifier.size(80.dp) // Circular button size
                ) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = "Save",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
