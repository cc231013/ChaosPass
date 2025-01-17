package at.ac.fhstp.chaospass.utils.chaosmodeGames


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ac.fhstp.chaospass.utils.copyToClipboard

@Composable
fun CatchMeIfYouCan(
    currentPassword: String,
    onComplete: () -> Unit
) {
    val context = LocalContext.current
    val handler = remember { android.os.Handler(android.os.Looper.getMainLooper()) }
    var randomOffsetX by remember { mutableStateOf(10.dp) }
    var randomOffsetY by remember { mutableStateOf(10.dp) }
    var randomColor by remember {
        mutableStateOf(
            Color((0..255).random(), (0..255).random(), (0..255).random())
        )
    }
    var randomFontSize by remember { mutableStateOf((16..32).random().toFloat()) } // Convert to Float

    // Update the position, color, and font size every 2 seconds
    DisposableEffect(Unit) {
        val runnable = object : Runnable {
            override fun run() {
                randomOffsetX = (10..300).random().dp
                randomOffsetY = (10..120).random().dp
                randomColor = Color((0..255).random(), (0..255).random(), (0..255).random())
                randomFontSize = (16..32).random().toFloat() // Convert to Float here as well
                handler.postDelayed(this, 200L)
            }
        }
        handler.post(runnable)
        onDispose {
            handler.removeCallbacksAndMessages(null)
        }
    }

    Box(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = currentPassword,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = randomFontSize.sp),
            color = randomColor,
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(x = randomOffsetX, y = randomOffsetY)
                .clickable {
                    copyToClipboard(context, "Password", currentPassword)
                    onComplete()
                }
                .padding(8.dp)
        )
    }
}
