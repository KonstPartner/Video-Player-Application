package com.example.videoplayerapplication

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videoplayerapplication.screens.HomeScreen
import com.example.videoplayerapplication.screens.InternetScreen
import com.example.videoplayerapplication.screens.PlayerScreen
import com.example.videoplayerapplication.screens.StorageScreen
import com.example.videoplayerapplication.screens.StreamingScreen
import com.example.videoplayerapplication.ui.theme.ThemeViewModel
import com.example.videoplayerapplication.ui.theme.VideoPlayerApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel() // Правильное получение ViewModel

            VideoPlayerApplicationTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "homeScreen") {
                        composable("homeScreen") {
                            // Передаем themeViewModel в HomeScreen
                            HomeScreen(navController, themeViewModel)
                        }
                        composable("storageScreen") { StorageScreen(navController) }
                        composable("internetScreen") { InternetScreen(navController) }
                        // Добавьте маршрут для PlayerScreen, если он ещё не добавлен
                        composable("playerScreen/{videoUri}") { backStackEntry ->
                            PlayerScreen(videoUri = backStackEntry.arguments?.getString("videoUri") ?: "")
                        }
                        composable("streamingScreen/{videoUrl}") { backStackEntry ->
                            StreamingScreen(videoUrl = Uri.decode(backStackEntry.arguments?.getString("videoUrl") ?: ""))
                        }

                    }
                }
            }
        }

    }
}
