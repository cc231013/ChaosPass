package at.ac.fhstp.chaospass.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String,
    chaosModeEnabled: State<Boolean> // Pass the chaos mode state
) {
    val backgroundColor = if (chaosModeEnabled.value) ChaosAddBlue else BackgroundNavy
    val contentColor = if (chaosModeEnabled.value) ChaosOnColour else Color.Black

    NavigationBar(
        containerColor = backgroundColor,
        contentColor = contentColor
    ) {
        NavigationBarItem(
            selected = currentRoute == "list",
            onClick = { navController.navigate("list") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Password List") },
            label = { Text("List") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = contentColor.copy(alpha = 0.1f),
                selectedIconColor = contentColor,
                unselectedIconColor = contentColor.copy(alpha = 0.6f),
                selectedTextColor = contentColor,
                unselectedTextColor = contentColor.copy(alpha = 0.6f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = contentColor.copy(alpha = 0.1f),
                selectedIconColor = contentColor,
                unselectedIconColor = contentColor.copy(alpha = 0.6f),
                selectedTextColor = contentColor,
                unselectedTextColor = contentColor.copy(alpha = 0.6f)
            )
        )
    }
}
