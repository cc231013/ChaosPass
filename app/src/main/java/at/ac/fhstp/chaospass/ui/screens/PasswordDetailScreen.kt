package at.ac.fhstp.chaospass.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel

@Composable
fun PasswordDetailScreen(
    entryId: Int,
    viewModel: EntryViewModel,
    navController: NavHostController
) {
    val entry = viewModel.entries.collectAsState().value.find { it.id == entryId }
    val isPasswordVisible = remember { mutableStateOf(false) }

    ScreenWrapper(
        navController = navController,
        currentRoute = "details/{entryId}"
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderBox(icon = Icons.Default.Key, rotation = 135f, backgroundColor = KeyBlue)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoField(label = "Site Name", value = entry?.siteName.orEmpty(), backgroundColor = BackgroundBlue, labelColor = BackgroundNavy)

                    InfoField(label = "Username", value = entry?.username.orEmpty(), backgroundColor = BackgroundBlue, labelColor = BackgroundNavy)

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        InfoField(
                            label = "Password",
                            value = if (isPasswordVisible.value) entry?.password.orEmpty() else "••••••••",
                            modifier = Modifier.weight(1f),
                            backgroundColor = BackgroundBlue,
                            labelColor = BackgroundNavy
                        )
                        IconButton(onClick = { isPasswordVisible.value = !isPasswordVisible.value }) {
                            Icon(
                                imageVector = if (isPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (isPasswordVisible.value) "Hide Password" else "Show Password"
                            )
                        }
                    }
                }
            }

            // Buttons: Edit and Delete
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("edit/$entryId") },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = AddGreen),
                    modifier = Modifier.size(80.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(32.dp),
                    )
                }

                Button(
                    onClick = {
                        entry?.let {
                            viewModel.deleteEntry(it)
                            navController.popBackStack()
                        }
                    },
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
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
        }
    }
}


