package com.example.videoplayerapplication.screens


import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun InternetScreen(navController: NavController) {
    var videoUrl by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Введите URL видео для потоковой передачи:")
        OutlinedTextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                // Здесь может быть логика проверки URL перед навигацией
                navController.navigate("streamingScreen/${Uri.encode(videoUrl)}")
                }),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Здесь может быть логика проверки URL перед навигацией
                navController.navigate("streamingScreen/${Uri.encode(videoUrl)}")
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Воспроизвести")
        }
    }
}



@Composable @Preview
fun InternetScreenPreview() {
    InternetScreen(navController = rememberNavController())
}
