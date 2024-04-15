package com.example.videoplayerapplication.screens

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.videoplayerapplication.ui.components.TopAppBar
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import kotlinx.coroutines.delay
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.content.Context
import androidx.activity.compose.BackHandler


@Composable
fun PlayerScreen(navController: NavController, videoUri: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    var isAppBarVisible by remember { mutableStateOf(true) }
    var lastTouchTime by remember { mutableStateOf(0L) }

    val onTouch = {
        lastTouchTime = System.currentTimeMillis()
        isAppBarVisible = true
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTouchTime > 4400) {
                isAppBarVisible = false
            }
        }
    }

    BackHandler {
        navController.navigateUp()
        showSystemUI(context)
    }

    AndroidView(factory = { ctx ->
        PlayerView(ctx).apply {
            player = exoPlayer
            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
            setBackgroundColor(android.graphics.Color.BLACK)
            systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
                    android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            setOnTouchListener { _, _ ->
                onTouch()
                hideSystemUI(ctx)
                false
            }
        }
    }, modifier = Modifier.fillMaxSize())

    if (isAppBarVisible) {
        TopAppBar(
            title = "",
            navigationIcon = Icons.Filled.ArrowBack,
            onNavigationIconClick = {
                navController.navigateUp()
                showSystemUI(context)
            },
            showMenu = false,
            backgroundColor = Color.Black
        )
    }
}

fun hideSystemUI(context: Context) {
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        hide(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
    }
}

fun showSystemUI(context: Context) {
    val window = (context as Activity).window
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        show(WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars())
    }
}
