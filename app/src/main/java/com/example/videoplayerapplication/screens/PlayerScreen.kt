package com.example.videoplayerapplication.screens

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Box
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player


@SuppressLint("ClickableViewAccessibility")
@Composable
fun PlayerScreen(navController: NavController, videoUri: String) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var showErrorDialog by remember { mutableStateOf(false) }


    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().also { player ->
            val mediaItem = MediaItem.fromUri(Uri.parse(videoUri))
            player.setMediaItem(mediaItem)
            player.prepare()
            player.playWhenReady = true
        }
    }

    var playbackPosition by rememberSaveable { mutableStateOf(0L) }
    var currentWindow by rememberSaveable { mutableStateOf(0) }
    var playWhenReady by rememberSaveable { mutableStateOf(true) }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                isLoading = state == Player.STATE_BUFFERING || state == Player.STATE_IDLE
                if (isLoading){
                    playbackPosition = exoPlayer.currentPosition
                    currentWindow = exoPlayer.currentMediaItemIndex
                    playWhenReady = exoPlayer.playWhenReady
                }
            }
            override fun onPlayerError(error: PlaybackException) {
                showErrorDialog = true
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            playbackPosition = exoPlayer.currentPosition
            currentWindow = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady

            exoPlayer.removeListener(listener)
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
        exoPlayer.seekTo(currentWindow, playbackPosition)
        exoPlayer.playWhenReady = playWhenReady
        while (true) {
            delay(1000)
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastTouchTime > 2000) {
                isAppBarVisible = false
            }
        }
    }

    BackHandler {
        navController.navigateUp()
        showSystemUI(context)
    }

    Box {
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

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }

        if (showErrorDialog) {
            ErrorDialog(onDismiss = {
                showErrorDialog = false
                navController.navigateUp()
                showSystemUI(context)
            })
        }

        if (isAppBarVisible) {
            TopAppBar(
                title = "",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClick = {
                    navController.navigateUp()
                    showSystemUI(context)
                },
                showMenu = false,
                backgroundColor = Color.Black,
                iconColor = Color.White
            )
        }
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

@Composable
fun ErrorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        backgroundColor = MaterialTheme.colorScheme.secondary,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "OK",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = "Ошибка воспроизведения",
                color = MaterialTheme.colorScheme.primary
                )
        },
        text = {
            Text(
                text = "Не удалось загрузить видео. Пожалуйста, проверьте корректность переданной ссылки, ваше соединение и попробуйте снова.",
                color = MaterialTheme.colorScheme.primary
            )
        }
    )
}