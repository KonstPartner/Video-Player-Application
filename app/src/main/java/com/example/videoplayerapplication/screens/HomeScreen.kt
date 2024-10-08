package com.example.videoplayerapplication.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.videoplayerapplication.R
import com.example.videoplayerapplication.ui.components.BottomAppBar
import com.example.videoplayerapplication.ui.components.TopAppBar
import com.example.videoplayerapplication.ui.theme.ThemeViewModel

@Composable
fun HomeScreen(navController: NavController, themeViewModel: ThemeViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = "Главная", Icons.Filled.Home, {}, false)
        },
        bottomBar = {
            BottomAppBar()
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.align(Alignment.Center)
            ) {
                Button(
                    onClick = { navController.navigate("storageScreen") },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(60.dp)
                        .fillMaxWidth(fraction = 0.8f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Хранилище", style = TextStyle(fontSize = 18.sp), color = MaterialTheme.colorScheme.primary)
                }
                Button(
                    onClick = { navController.navigate("internetScreen") },
                    modifier = Modifier
                        .padding(16.dp)
                        .height(60.dp)
                        .fillMaxWidth(fraction = 0.8f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    border = BorderStroke(1.dp, Color.Black),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Интернет", style = TextStyle(fontSize = 18.sp), color = MaterialTheme.colorScheme.primary)
                }
            }
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_moon),
                contentDescription = "Смена темы",
                modifier = Modifier
                    .clip(CircleShape)
                    .align(Alignment.BottomEnd)
                    .clickable(onClick = themeViewModel::toggleTheme)
                    .padding(16.dp)
                    .size(36.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController(), ThemeViewModel())
}
