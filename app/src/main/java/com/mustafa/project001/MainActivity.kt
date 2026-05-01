package com.mustafa.project001

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import com.mustafa.project001.ui.theme.Project001Theme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mustafa.project001.navigation.BottomNavigationBar
import com.mustafa.project001.ui.screens.CameraScreen
import com.mustafa.project001.ui.screens.Dictionary
import com.mustafa.project001.ui.screens.HomeScreen
import com.mustafa.project001.ui.screens.ProfileScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Project001Theme {
                SetBarColor(color = MaterialTheme.colorScheme.background)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
    @Composable
    private fun SetBarColor(color: Color) {
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color
            )
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
//        color = Color.Black.copy(alpha = 0.8f)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 2- NavHost
                // Define the nav Graph, start destination & composable destination
                NavHost(
                    navController = navController,
                    startDestination = "camera"
                ){
                    // The Home Screen
                    composable("home") { HomeScreen() }
                    // Define the Camera Destination Composable
                    composable("camera") { CameraScreen() }

                    composable("dictionary") { Dictionary() }
                    // Define the Profile Destination Composable
                    composable("profile") { ProfileScreen() }
                }
            }

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF764BA2)
@Composable
fun AppPreview() {
    MainScreen()
}