package at.ac.fhstp.chaospass.ui.components

import BackgroundCircles
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController


@Composable
fun ScreenWrapper(
    onBackClick: (() -> Unit)? = null,
    navController: NavHostController,
    currentRoute: String,
    chaosModeEnabled: State<Boolean>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick, chaosModeEnabled = chaosModeEnabled.value) },
        bottomBar = { BottomNavigationBar(
            navController = navController, currentRoute = currentRoute,
            chaosModeEnabled = chaosModeEnabled) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (chaosModeEnabled.value) {
                ChaosModeBackground()
            } else {
                BackgroundCircles()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            ) {
                content(paddingValues)
            }
        }
    }
}

