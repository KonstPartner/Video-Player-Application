package com.example.videoplayerapplication.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.videoplayerapplication.ui.components.TopAppBar
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout

@Composable
fun PlayerScreen(navController: NavController, videoUri: String) {
    val context = LocalContext.current
    var showControls by remember { mutableStateOf(true) } // Изначально контролы видимы

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) {
            showControls = !showControls
        }) {
        AndroidView(factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setBackgroundColor(android.graphics.Color.BLACK)
                useController = showControls
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
            }
        }, modifier = Modifier.fillMaxSize())

        if (showControls) {
            TopAppBar(
                title = "",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClick = { navController.navigateUp() },
                showMenu = false
            ){}
        }
    }
}
