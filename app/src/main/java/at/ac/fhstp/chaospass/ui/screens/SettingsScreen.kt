package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.ui.viewmodel.SettingsViewModel


@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel
) {
    // Remember the chaos mode state
    val chaosModeEnabled = viewModel.isChaosMode.collectAsState()

    ScreenWrapper(
        navController = navController,
        currentRoute = "settings"
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBox(icon = Icons.Default.Settings, backgroundColor = KeyBlue)
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge
            )

            // Chaos Mode Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "Enable Chaos Mode",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = chaosModeEnabled.value,
                    onCheckedChange = { viewModel.toggleChaosMode(it) },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}
