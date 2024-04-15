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
import com.example.videoplayerapplication.ui.theme.ThemeViewModel
import com.example.videoplayerapplication.ui.theme.VideoPlayerApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            VideoPlayerApplicationTheme(darkTheme = themeViewModel.isDarkTheme.value) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "homeScreen") {
                        composable("homeScreen") {
                            HomeScreen(navController, themeViewModel)
                        }
                        composable("storageScreen") { StorageScreen(navController) }
                        composable("internetScreen") { InternetScreen(navController) }
                        composable("playerScreen/{videoUri}") { backStackEntry ->
                            PlayerScreen(navController, videoUri = backStackEntry.arguments?.getString("videoUri") ?: "")
                        }
                    }
                }
            }
        }

    }
}
