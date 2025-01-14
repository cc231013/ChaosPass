package at.ac.fhstp.chaospass.ui.components

import BackgroundCircles
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenWrapper(
    onBackClick: (() -> Unit)? = null,
    navController: NavHostController,
    currentRoute: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { TopBar(onBackClick = onBackClick) },
        bottomBar = { BottomNavigationBar(navController = navController, currentRoute = currentRoute) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Draw background circles
            BackgroundCircles()

            // Screen content layered on top
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
