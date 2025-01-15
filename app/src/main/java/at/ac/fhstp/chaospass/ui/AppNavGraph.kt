package at.ac.fhstp.chaospass.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import at.ac.fhstp.chaospass.data.repository.EntryRepository
import at.ac.fhstp.chaospass.ui.screens.AddPasswordScreen
import at.ac.fhstp.chaospass.ui.screens.EditPasswordScreen
import at.ac.fhstp.chaospass.ui.screens.PasswordDetailScreen
import at.ac.fhstp.chaospass.ui.screens.PasswordListScreen
import at.ac.fhstp.chaospass.ui.screens.SettingsScreen
import at.ac.fhstp.chaospass.ui.viewmodel.SettingsViewModel
import at.ac.fhstp.chaospass.ui.viewmodel.factory.EntryViewModelFactory
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavGraph(repository: EntryRepository, chaosModeEnabled: Boolean) {
    val navController = rememberNavController()

    val viewModelFactory = EntryViewModelFactory(repository)
    val viewModel: EntryViewModel = viewModel(factory = viewModelFactory)
    val settingsViewModel: SettingsViewModel = viewModel()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            viewModel.fetchEntries()
            PasswordListScreen(
                navController = navController,
                viewModel = viewModel,
                chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState()
            )
        }
        composable("details/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId")?.toIntOrNull()
            entryId?.let {
                PasswordDetailScreen(
                    entryId = it,
                    viewModel = viewModel,
                    navController = navController,
                    chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState()
                )
            }
        }
        composable("edit/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId")?.toIntOrNull()
            entryId?.let {
                EditPasswordScreen(
                    entryId = it,
                    viewModel = viewModel,
                    navController = navController,
                    chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState()
                )
            }
        }
        composable("settings") {
            SettingsScreen(navController = navController, viewModel = settingsViewModel)
        }
        composable("add") {
            AddPasswordScreen(
                viewModel = viewModel,
                navController = navController,
                chaosModeEnabled = settingsViewModel.isChaosMode.collectAsState(),
            )
        }
    }
}
