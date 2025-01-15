package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBackClick: (() -> Unit)? = null,
    chaosModeEnabled: Boolean
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chaosModeEnabled) {
                    // Swap positions: Logo on the left, Back Button on the right
                    LogoIcon(chaosModeEnabled = chaosModeEnabled)

                    if (onBackClick != null) {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = getColorBasedOnMode(chaosModeEnabled, Color.Black, ChaosKeyPink),
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp)) // Placeholder for consistent alignment
                    }
                } else {
                    // Normal Mode: Back Button on the left, Logo on the right
                    if (onBackClick != null) {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = getColorBasedOnMode(chaosModeEnabled, Color.Black, ChaosKeyPink),
                                modifier = Modifier.size(48.dp),
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(48.dp)) // Placeholder for consistent alignment
                    }

                    LogoIcon(chaosModeEnabled = chaosModeEnabled)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

