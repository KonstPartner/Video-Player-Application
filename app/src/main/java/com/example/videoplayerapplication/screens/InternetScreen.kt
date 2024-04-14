package com.example.videoplayerapplication.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.videoplayerapplication.ui.components.BottomAppBar
import com.example.videoplayerapplication.ui.components.TopAppBar

@Composable
fun InternetScreen(navController: NavController) {
    var videoUrl by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = "Потоковое видео",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClick = { navController.navigateUp() },
                showMenu = false,
                onChangeColumns = {}
            )
        },
        bottomBar = {
            BottomAppBar()
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 17.dp),
                text = "Плеер потокового видео:",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 17.sp
            )
            OutlinedTextField(
                value = videoUrl,
                onValueChange = { videoUrl = it },
                label = { Text("Введите URL видео") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (videoUrl.isNotEmpty()) {
                        navController.navigate("playerScreen/${Uri.encode(videoUrl)}")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Воспроизвести")
            }
        }
    }
}


@Preview @Composable
fun InternetScreenPreview() {
    InternetScreen(rememberNavController())
}