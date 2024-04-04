package com.example.videoplayerapplication.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext

@Composable
fun StorageScreen(navController: NavController) {
    val context = LocalContext.current
    val videos = getVideos(context)

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(videos) { video ->
                VideoItem(video = video, navController = navController)
            }
        }
    }
}

@Composable
fun VideoItem(video: Video, navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("playerScreen/${Uri.encode(video.uri.toString())}")
            }
    ) {
        video.thumbnail?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
        Text(text = video.title, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

