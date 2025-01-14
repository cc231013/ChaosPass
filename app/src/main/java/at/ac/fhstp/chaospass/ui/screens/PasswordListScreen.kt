package at.ac.fhstp.chaospass.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.CustomOutlinedTextField
import at.ac.fhstp.chaospass.ui.components.HeaderBox
import at.ac.fhstp.chaospass.ui.components.ScreenWrapper
import at.ac.fhstp.chaospass.ui.theme.AddGreen
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel
import kotlinx.coroutines.launch

@Composable
fun PasswordListScreen(
    navController: NavHostController,
    viewModel: EntryViewModel
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortAscending by remember { mutableStateOf(true) }
    val entries by viewModel.entries.collectAsState()

    // Filter and sort entries
    val filteredEntries = remember(searchQuery, sortAscending, entries) {
        entries.filter {
            it.siteName.contains(searchQuery.text, ignoreCase = true)
        }.sortedWith(
            if (sortAscending) compareBy { it.siteName.lowercase() }
            else compareByDescending { it.siteName.lowercase() }
        )
    }

    val focusManager = LocalFocusManager.current

    ScreenWrapper(
        navController = navController,
        currentRoute = "list"
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus() // Clear focus when clicking outside
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderBox(icon = Icons.Default.Key, rotation = 135f, backgroundColor = KeyBlue)

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomOutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = "Search by Site Name",
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { /* Handle focus changes if needed */ },
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { sortAscending = !sortAscending }) {
                        Icon(
                            imageVector = if (sortAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = if (sortAscending) "Sort Ascending" else "Sort Descending"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (filteredEntries.isEmpty()) {
                        item {
                            Text("No entries found. Add some passwords!")
                        }
                    } else {
                        items(filteredEntries, key = { it.id }) { entry ->
                            SwipeableEntryItem(
                                entry = entry,
                                onSwipeRight = {
                                    clipboardManager.setText(AnnotatedString(entry.password))
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Password copied to clipboard!")
                                    }
                                },
                                onClick = {
                                    navController.navigate("details/${entry.id}")
                                }
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate("add") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = AddGreen,
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Password")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableEntryItem(
    entry: Entry,
    onSwipeRight: () -> Unit,
    onClick: () -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 100f // Adjust the threshold as needed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        // Only allow positive (rightward) drag
                        if (dragAmount > 0) {
                            offsetX += dragAmount
                        }
                    },
                    onDragEnd = {
                        if (offsetX > swipeThreshold) {
                            onSwipeRight() // Trigger the action for right swipe
                        }
                        offsetX = 0f // Reset offset
                    }
                )
            }
    ) {
        // Background for the swipe action
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.CenterStart
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy Password",
                tint = Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Foreground: Entry card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX.dp) // Apply the drag offset
                .padding(vertical = 4.dp)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                text = entry.siteName,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}









