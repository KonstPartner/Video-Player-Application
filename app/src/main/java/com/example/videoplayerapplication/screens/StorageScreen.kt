package com.example.videoplayerapplication.screens

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.videoplayerapplication.server.VideoStreamServer
import com.example.videoplayerapplication.ui.components.BottomAppBar
import com.example.videoplayerapplication.ui.components.TopAppBar
import com.example.videoplayerapplication.ui.components.Video
import com.example.videoplayerapplication.ui.components.getVideos
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay
import java.net.NetworkInterface

enum class SortOrder(val title: String) {
    ByName("По имени"),
    ByDate("По дате"),
    ByDuration("По длительности")
}

fun onVideoSelected(context: Context, videoUri: Uri, server: MutableState<VideoStreamServer?>) {
    val videoPath = getPathFromUri(context, videoUri)
    if (videoPath != null) {
        server.value?.stopServer()
        server.value = VideoStreamServer(8080)
        server.value?.videoFilePath = videoPath
        server.value?.start()
        Toast.makeText(context, "Передача запущена", Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, "Unable to get file path.", Toast.LENGTH_SHORT).show()
    }
}




fun getPathFromUri(context: Context, uri: Uri): String? {
    val projection = arrayOf(MediaStore.Video.Media.DATA)
    val cursor: Cursor? = context.contentResolver.query(uri, projection, null, null, null)
    return if (cursor != null) {
        val columnIndex: Int = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(columnIndex)
        cursor.close()
        path
    } else
        null
}

fun getLocalIpAddress(): String? {
    val ipAddress = NetworkInterface.getNetworkInterfaces().toList()
        .filter { it.inetAddresses.hasMoreElements() && !it.isLoopback }
        .flatMap { it.inetAddresses.toList() }
        .find { !it.isLoopbackAddress && it.isSiteLocalAddress }
        ?.let { return it.hostAddress.toString() }
    Log.d("NetworkInfo", "IP Address: $ipAddress")

    return ipAddress
}

@Composable
fun StorageScreen(navController: NavController) {
    val context = LocalContext.current
    var columns by rememberSaveable { mutableStateOf(1) }
    var searchText by rememberSaveable { mutableStateOf("") }
    var searchTextDebounced by rememberSaveable { mutableStateOf("") }
    var sortOrder by rememberSaveable { mutableStateOf(SortOrder.ByName) }
    var ascending by rememberSaveable { mutableStateOf(true) }
    val server = remember { mutableStateOf<VideoStreamServer?>(null) }
    val showStreamingWarningDialog = remember { mutableStateOf(false) }
    val activity = context as? ComponentActivity
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(searchText, server.value) {
        delay(300)
        searchTextDebounced = searchText
        if (server.value != null) {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        } else {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    BackHandler {
        if (server.value != null) {
            showStreamingWarningDialog.value = true
        } else {
            navController.navigateUp()
        }
    }

    val filteredSortedVideos by remember(searchTextDebounced, sortOrder, ascending) {
        mutableStateOf(
            getVideos(context)
                .filter { searchTextDebounced.isEmpty() || it.title.contains(searchTextDebounced, ignoreCase = true) }
                .sortedWith(Comparator { a, b ->
                    when (sortOrder) {
                        SortOrder.ByName -> if (ascending) a.title.compareTo(b.title) else b
                            .title.compareTo(a.title)
                        SortOrder.ByDate -> if (!ascending) a.date.compareTo(b.date) else b.date
                            .compareTo(a.date)
                        SortOrder.ByDuration -> if (!ascending) a.duration.compareTo(b.duration)
                        else b.duration.compareTo(a.duration)
                    }
                })
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = "Выбор видео",
                navigationIcon = Icons.Filled.ArrowBack,
                onNavigationIconClick = {
                    if (server.value != null) {
                        showStreamingWarningDialog.value = true
                    } else {
                        navController.navigateUp()
                    }
                },
                showMenu = true,
                onChangeColumns = { columns = it },
                onSortChange = { selectedSortOrder, isAsc ->
                    if (sortOrder == selectedSortOrder) {
                        ascending = !ascending
                    } else {
                        sortOrder = selectedSortOrder
                        ascending = isAsc
                    }
                }
            )
        },
        bottomBar = {}
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SearchBar(searchText = searchText, onSearchTextChanged = { searchText = it })
            if (server.value != null) {
                val ipAddress = getLocalIpAddress()
                val url = "http://$ipAddress:8080"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 60.dp, end = 60.dp, bottom = 5.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                        .padding(vertical = 5.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ClickableText(
                        text = AnnotatedString(url),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 13.sp,
                            fontStyle = FontStyle.Italic,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                            fontWeight = MaterialTheme.typography.bodyLarge.fontWeight
                        ),
                        modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
                        onClick = {
                            clipboardManager.setText(AnnotatedString(url))
                            Toast.makeText(context, "URL скопирован в буфер обмена", Toast.LENGTH_SHORT).show()
                        }
                    )
                    Button(
                        onClick = {
                            server.value?.stopServer()
                            server.value = null
                            Toast.makeText(context, "Передача остановлена", Toast.LENGTH_SHORT)
                                .show()
                        },
                        modifier = Modifier.padding(start = 2.dp, end = 2.dp).size(width = 150
                            .dp, height = 30.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Остановить передачу",
                            color = Color.White,
                            style = TextStyle(fontSize = 8.sp)
                        )
                    }
                }

            }
            VideoGrid(videos = filteredSortedVideos, columns = columns, navController =
            navController, server = server, showStreamingWarningDialog = showStreamingWarningDialog)
            if (showStreamingWarningDialog.value) {
                AlertDialog(
                    onDismissRequest = { showStreamingWarningDialog.value = false },
                    title = { Text("Внимание", color = MaterialTheme.colorScheme.primary) },
                    text = { Text("Для продолжения остановите передачу видео.", color = MaterialTheme.colorScheme.primary) },
                    confirmButton = {
                        Button(
                            onClick = { showStreamingWarningDialog.value = false },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme
                                .primary)
                        ) {
                            Text("ОК", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoItem(video: Video, columns: Int, navController: NavController, context: Context, server: MutableState<VideoStreamServer?>, showStreamingWarningDialog: MutableState<Boolean>) {
    val textStyle = getTextStyleForColumns(columns)
    val cardHeight = calculateCardHeight(context, columns)
    var showMenu by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(cardHeight.dp)
            .combinedClickable(
                onClick = {
                    if (server.value != null) {
                        showStreamingWarningDialog.value = true
                    } else {
                        navController.navigate("playerScreen/${Uri.encode(video.uri.toString())}")
                    }
                },
                onLongClick = { showMenu = true },
            ),
        border = BorderStroke(1.dp, Color.Black),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
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
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        color = Color.White
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
                modifier = Modifier.padding(horizontal = 4.dp),
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis
            )
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    showMenu = false
                    if (server.value != null) {
                        showStreamingWarningDialog.value = true
                    } else {
                        onVideoSelected(context, video.uri, server)
                    }
                },
                text = { Text("Передавать", color = MaterialTheme.colorScheme.primary) }
            )
        }
    }
}

@Composable
fun VideoGrid(videos: List<Video>, columns: Int, navController: NavController, server:
MutableState<VideoStreamServer?>, showStreamingWarningDialog: MutableState<Boolean>) {
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(8.dp),
    ) {
        items(videos) { video ->
            VideoItem(video = video, columns, navController, context, server, showStreamingWarningDialog)
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

fun calculateCardHeight(context: Context, columns: Int): Float {
    val orientation = context.resources.configuration.orientation
    return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        when (columns) {
            1 -> 465f
            2 -> 250f
            else -> 175f
        }
    } else {
        when (columns) {
            1 -> 240f
            2 -> 140f
            else -> 100f
        }
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
        placeholder = { Text(text="Поиск...", color = MaterialTheme.colorScheme.primary) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Поиск", tint = MaterialTheme.colorScheme.primary) },
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