package at.ac.fhstp.chaospass.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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


@Composable
fun AppNavGraph(repository: EntryRepository) {
    val navController = rememberNavController()

    // Provide EntryViewModel with a factory
    val viewModelFactory = EntryViewModelFactory(repository)
    val viewModel: EntryViewModel = viewModel(factory = viewModelFactory)

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            viewModel.fetchEntries()
            PasswordListScreen(navController = navController, viewModel = viewModel)
        }
        composable("details/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId")?.toIntOrNull()
            entryId?.let {
                PasswordDetailScreen(entryId = it, viewModel = viewModel, navController = navController)
            }
        }
        composable("edit/{entryId}") { backStackEntry ->
            val entryId = backStackEntry.arguments?.getString("entryId")?.toIntOrNull()
            entryId?.let {
                EditPasswordScreen(entryId = it, viewModel = viewModel, navController = navController)
            }
        }
        composable("settings") {
            SettingsScreen(navController = navController,viewModel= SettingsViewModel())
        }
        composable("add") {
            AddPasswordScreen(
                viewModel = EntryViewModel(repository), // Provide the ViewModel
                onCancel = { navController.navigateUp() },
                navController = navController
            )
        }

    }
}
