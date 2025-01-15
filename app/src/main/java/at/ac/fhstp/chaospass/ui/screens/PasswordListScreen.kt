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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import at.ac.fhstp.chaospass.ui.theme.ChaosAddBlue
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.KeyBlue
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode
import at.ac.fhstp.chaospass.viewmodel.EntryViewModel
import kotlinx.coroutines.launch

@Composable
fun PasswordListScreen(
    navController: NavHostController,
    viewModel: EntryViewModel,
    chaosModeEnabled: State<Boolean>
) {
    val clipboardManager = LocalClipboardManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var sortAscending by remember { mutableStateOf(true) }
    val entries by viewModel.entries.collectAsState()
    var verticalOffset by remember { mutableStateOf(0f) }

    // Filter and sort entries
    val filteredEntries = remember(searchQuery, sortAscending, entries, chaosModeEnabled.value) {
        entries.filter {
            if (chaosModeEnabled.value) {
                // Chaos mode: Search by password
                it.password.contains(searchQuery.text, ignoreCase = true)
            } else {
                // Normal mode: Search by site name
                it.siteName.contains(searchQuery.text, ignoreCase = true)
            }
        }.sortedWith(
            if (sortAscending) compareBy {
                if (chaosModeEnabled.value) it.password.lastOrNull()?.lowercase() ?: ""
                else it.siteName.lastOrNull()?.lowercase() ?: ""
            } else compareByDescending {
                if (chaosModeEnabled.value) it.password.lastOrNull()?.lowercase() ?: ""
                else it.siteName.lastOrNull()?.lowercase() ?: ""
            }
        )
    }



    val focusManager = LocalFocusManager.current

    ScreenWrapper(
        navController = navController,
        currentRoute = "list",
        chaosModeEnabled = chaosModeEnabled
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
                HeaderBox(
                    icon = Icons.Default.Key, rotation = 135f, backgroundColor = getColorBasedOnMode(
                    chaosModeEnabled.value,
                    KeyBlue,
                    ChaosKeyPink
                )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomOutlinedTextField(
                        value = searchQuery,
                        onValueChange = { newValue ->
                            if (chaosModeEnabled.value) {
                                // Apply random uppercase and lowercase transformation
                                val transformedValue = newValue.text.map { char ->
                                    if ((0..1).random() == 0) char.uppercaseChar() else char.lowercaseChar()
                                }.joinToString("")
                                searchQuery = TextFieldValue(transformedValue, newValue.selection)
                            } else {
                                // Allow normal input in normal mode
                                searchQuery = newValue
                            }

                            // Update the vertical offset based on sorting order
                            verticalOffset += if (sortAscending) -1f else 1f // Move slightly up or down
                        },
                        label = if (chaosModeEnabled.value) "Search by Password Character" else "Search by Site Name",
                        modifier = Modifier
                            .weight(1f)
                            .offset(y = verticalOffset.dp) // Apply vertical movement
                            .onFocusChanged { /* Handle focus changes if needed */ }
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
                                },
                                chaosModeEnabled = chaosModeEnabled.value
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
                containerColor = getColorBasedOnMode(chaosModeEnabled.value, AddGreen, ChaosAddBlue),
                contentColor = Color.Black
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Password")
            }
        }
    }
}

@Composable
fun SwipeableEntryItem(
    entry: Entry,
    onSwipeRight: () -> Unit,
    onClick: () -> Unit,
    chaosModeEnabled: Boolean
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 100f
    var isTransparent by remember { mutableStateOf(false) } // Transparency state for chaos mode

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
                            if (chaosModeEnabled) {
                                // Make the entry visually transparent
                                isTransparent = true
                            } else {
                                onSwipeRight() // Normal behavior
                            }
                        }
                        offsetX = 0f
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
                tint = if (chaosModeEnabled || isTransparent) Color.Transparent else Color.Gray,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Foreground: Entry card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX.dp)
                .padding(vertical = 4.dp)
                .clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (chaosModeEnabled && isTransparent) Color.Transparent else MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = entry.siteName,
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        if (chaosModeEnabled && isTransparent) Color.Transparent else Color.Unspecified
                    ),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = if (chaosModeEnabled && isTransparent) Color.Transparent else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
