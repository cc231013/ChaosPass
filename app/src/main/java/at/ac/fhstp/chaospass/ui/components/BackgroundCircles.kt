import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.ui.theme.BackgroundBlue
import at.ac.fhstp.chaospass.ui.theme.Circles

@Composable
fun BackgroundCircles() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Background color
    ) {
        // Large circle at the bottom
        Box(
            modifier = Modifier
                .size(350.dp) // Ensure width and height are equal
                .offset(x = (-50).dp, y = 470.dp) // Adjust position
                .background(Circles, shape = CircleShape) // Orange color with CircleShape
        )

        // Small circle at the top
        Box(
            modifier = Modifier
                .size(150.dp) // Ensure width and height are equal
                .offset(x = 290.dp, y = (15).dp) // Adjust position
                .background(Circles, shape = CircleShape) // Light orange color with CircleShape
        )
        Box(
            modifier = Modifier
                .size(200.dp) // Ensure width and height are equal
                .offset(x = (80).dp, y = (550).dp) // Adjust position
                .background(BackgroundBlue, shape = CircleShape) // Light orange color with CircleShape
        )
    }
}
