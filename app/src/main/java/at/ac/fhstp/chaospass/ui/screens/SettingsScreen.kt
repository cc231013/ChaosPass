package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.ui.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel
) {
    val chaosModeEnabled = viewModel.isChaosMode.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showExplanationDialog by remember { mutableStateOf(false) }

    ScreenWrapper(
        onBackClick = { navController.popBackStack() },
        navController = navController,
        currentRoute = "settings",
        chaosModeEnabled = chaosModeEnabled
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Main Content at the Top
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Box with Settings Icon
                val headerColor = if (chaosModeEnabled.value) ChaosKeyPink else KeyBlue
                HeaderBox(icon = Icons.Default.Settings, backgroundColor = headerColor)

                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge
                )

                // Chaos Mode Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Enable Chaos Mode",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Switch(
                        checked = chaosModeEnabled.value,
                        onCheckedChange = { newState ->
                            if (newState) {
                                showDialog = true
                            } else {
                                viewModel.toggleChaosMode(newState)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                            checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                            uncheckedTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            // Elements at the Bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status Text
                Text(
                    text = if (chaosModeEnabled.value) "Chaos Mode is currently ON" else "Chaos Mode is currently OFF",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (chaosModeEnabled.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Explanation Icon and Text
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "What is Chaos Mode?",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showExplanationDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Help,
                            contentDescription = "Explain Chaos Mode",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Confirmation Dialog for Chaos Mode
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Activate Chaos Mode?") },
                    text = { Text("Do you really want to activate Chaos Mode? It will enable randomized and unpredictable behavior.") },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.toggleChaosMode(true)
                            showDialog = false
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }

            // Explanation Dialog
            if (showExplanationDialog) {
                AlertDialog(
                    onDismissRequest = { showExplanationDialog = false },
                    title = { Text("What is Chaos Mode?") },
                    text = {
                        Text(
                            "Chaos Mode introduces randomized and unpredictable behavior to your app, " +
                                    "making it more challenging to interact with. Use it for fun or to test your patience!"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = { showExplanationDialog = false }) {
                            Text("Got it!")
                        }
                    }
                )
            }
        }
    }
}
