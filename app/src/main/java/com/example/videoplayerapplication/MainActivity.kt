package com.example.videoplayerapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videoplayerapplication.screens.MainScreen
import com.example.videoplayerapplication.screens.PlayerScreen
import com.example.videoplayerapplication.ui.theme.VideoPlayerApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VideoPlayerApplicationTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") { MainScreen(navController) }
                        composable("playerScreen/{videoUri}") { backStackEntry ->
                            PlayerScreen(videoUri = backStackEntry.arguments?.getString("videoUri") ?: "")
                        }
                    }
                }
            }
        }
    }
}
