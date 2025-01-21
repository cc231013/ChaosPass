package at.ac.fhstp.chaospass.utils.chaosmodeGames

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.data.entities.Entry
import at.ac.fhstp.chaospass.ui.components.InfoField
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.BackgroundNavy
import at.ac.fhstp.chaospass.ui.theme.ChaosEdit
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink
import at.ac.fhstp.chaospass.ui.theme.ChaosOnColour
import at.ac.fhstp.chaospass.utils.copyToClipboard
import at.ac.fhstp.chaospass.utils.getColorBasedOnMode

@Composable
fun FieldClicker(
    entry: Entry?,
    chaosModeEnabled: Boolean,
    hiddenFields: SnapshotStateList<Int>,
    hiddenFieldMessage: MutableState<String>
) {
    val buttonColor = remember { mutableStateOf(ChaosEdit) }
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth()) {
            InfoField(
                label = "Username",
                value = if (hiddenFields.isEmpty()) entry?.username.orEmpty() else "",
                backgroundColor = getColorBasedOnMode(
                    chaosModeEnabled,
                    BackgroundBlue,
                    ChaosKeyPink
                ),
                labelColor = getColorBasedOnMode(
                    chaosModeEnabled,
                    BackgroundNavy,
                    ChaosOnColour
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                InfoField(
                    label = "Password",
                    value = entry?.password.orEmpty(),
                    backgroundColor = getColorBasedOnMode(
                        chaosModeEnabled,
                        BackgroundBlue,
                        ChaosKeyPink
                    ),
                    labelColor = getColorBasedOnMode(
                        chaosModeEnabled,
                        BackgroundNavy,
                        ChaosOnColour
                    ),
                    isPasswordField = true,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            entry?.password?.let {
                                copyToClipboard(context, "Password", it)
                            }
                        }
                )
            }
        }

        // Overlay with clickable hidden fields
        if (hiddenFields.isNotEmpty()) {
            Box(modifier = Modifier.matchParentSize()) {
                hiddenFields.forEachIndexed { index, _ ->
                    Button(
                        onClick = {
                            hiddenFields.removeAt(index)
                            buttonColor.value = Color(
                                red = (0..255).random(),
                                green = (0..255).random(),
                                blue = (0..255).random()
                            )
                            hiddenFieldMessage.value = when {
                                hiddenFields.isEmpty() -> "All fields cleared!"
                                hiddenFields.size == 1 -> "One more to go!"
                                else -> "Keep going, only ${hiddenFields.size} left!"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = buttonColor.value),
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Click me")
                    }
                }
            }
        }
    }
}
