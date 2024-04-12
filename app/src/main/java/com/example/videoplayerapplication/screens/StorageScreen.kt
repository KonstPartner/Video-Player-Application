package com.example.videoplayerapplication.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.ui.text.TextStyle
import com.example.videoplayerapplication.ui.components.Video
import com.example.videoplayerapplication.ui.components.getVideos
import com.example.videoplayerapplication.ui.components.TopAppBar
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay


@Composable
fun StorageScreen(navController: NavController) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf("") }
    var searchTextDebounced by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        delay(300)
        searchTextDebounced = searchText
    }

    val filteredVideos by remember(searchTextDebounced) {
        mutableStateOf(getVideos(context).filter {
            searchTextDebounced.isEmpty() || it.title.contains(searchTextDebounced, ignoreCase = true)
        })
    }

    var columns by remember { mutableStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Выбор видео",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClick = { navController.navigateUp() },
                showMenu = true,
                onChangeColumns = { columns = it }
            )
        },
        bottomBar = {
            com.example.videoplayerapplication.ui.components.BottomAppBar()
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(searchText = searchText, onSearchTextChanged = { searchText = it })
            VideoGrid(videos = filteredVideos, columns = columns, navController = navController)
        }
    }
}


@Composable
fun VideoItem(video: Video, columns: Int, navController: NavController) {
    val textStyle = getTextStyleForColumns(columns)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { navController.navigate("playerScreen/${Uri.encode(video.uri.toString())}") },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(5.dp)
        ) {
            video.thumbnail?.let { bitmap ->
                Box {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Видео эскиз",
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = formatDuration(video.duration),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                    Icon(
                        Icons.Filled.PlayArrow,
                        contentDescription = "Воспроизвести",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp),
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                style = textStyle,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Composable
fun VideoGrid(videos: List<Video>, columns: Int, navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(videos) { video ->
            VideoItem(video = video, columns, navController)
        }
    }
}


@Composable
fun getTextStyleForColumns(columns: Int): TextStyle {
    return when (columns) {
        1 -> MaterialTheme.typography.bodyLarge
        2 -> MaterialTheme.typography.bodyMedium
        else -> MaterialTheme.typography.bodySmall
    }
}


fun formatDuration(durationMs: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(durationMs)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(durationMs) % 60
    return String.format("%02d:%02d", minutes, seconds)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChanged,
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text(text="Поиск...") },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск", tint = MaterialTheme.colorScheme.onSurface) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.medium)
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            keyboardController?.hide()
        })
    )
}


@Composable @Preview
fun StorageScreenPreview(){
    StorageScreen(navController = rememberNavController())
}