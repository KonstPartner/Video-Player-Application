package com.example.videoplayerapplication.screens

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
                showMenu = false
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
                color = MaterialTheme.colorScheme.primary,
                fontSize = 17.sp
            )
            Box(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .heightIn(max = 150.dp)
            ) {
                OutlinedTextField(
                    value = videoUrl,
                    onValueChange = { videoUrl = it },
                    label = { Text("Введите URL видео") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (videoUrl.isNotEmpty()) {
                        navController.navigate("playerScreen/${Uri.encode(videoUrl)}")
                    }
                },
                modifier = Modifier.align(Alignment.End),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
                border = BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Воспроизвести", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Preview @Composable
fun InternetScreenPreview() {
    InternetScreen(rememberNavController())
}