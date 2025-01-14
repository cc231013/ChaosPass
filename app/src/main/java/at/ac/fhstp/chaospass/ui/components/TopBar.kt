package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBackClick != null) {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(48.dp)) // Placeholder for consistent alignment
                }

            }
        },
        actions = {
            LogoIcon() // Add the logo to the top bar
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = BackgroundNavy,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}
