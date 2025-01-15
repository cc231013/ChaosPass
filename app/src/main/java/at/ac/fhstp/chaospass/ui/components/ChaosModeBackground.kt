package at.ac.fhstp.chaospass.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import at.ac.fhstp.chaospass.R
import at.ac.fhstp.chaospass.ui.theme.ChaosKeyPink

@Composable
fun ChaosModeBackground(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Icon(
            painter = painterResource(id = R.drawable.b86a33ebd1f240eab9d017b02a18ce0b_removebg_preview),
            contentDescription = null,
            tint = ChaosKeyPink,
            modifier = Modifier
                .size(800.dp)
        )
    }
}
