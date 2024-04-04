package com.example.videoplayerapplication.screens

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun StreamingScreen(videoUrl: String) {
    val context = LocalContext.current
    val exoPlayer = rememberExoPlayerForStreaming(context, videoUrl)

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(factory = { ctx ->
        PlayerView(ctx).apply {
            player = exoPlayer
        }
    }, modifier = Modifier.fillMaxSize())
}

@Composable
fun rememberExoPlayerForStreaming(context: Context, videoUrl: String): ExoPlayer {
    val exoPlayer = ExoPlayer.Builder(context).build().apply {
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        setMediaItem(mediaItem)
        prepare()
        playWhenReady = true
    }
    return exoPlayer
}
