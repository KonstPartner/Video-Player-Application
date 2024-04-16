package com.example.videoplayerapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    inColumnSettings: Boolean,
    onChangeColumns: (Int) -> Unit,
    onMenuChange: (Boolean) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.onBackground
    val menuItemHeight = 48.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            onMenuChange(false)
            onDismissRequest()
        },
        modifier = Modifier
            .width(240.dp)
            .background(backgroundColor)
    ) {
        if (!inColumnSettings) {
            DropdownMenuItem(
                text = { Text("Вид", color = textColor) },
                onClick = { onMenuChange(true) },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
        } else {
            DropdownMenuItem(
                text = { Text("Назад", color = textColor) },
                onClick = { onMenuChange(false) },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
            Divider(color = textColor.copy(alpha = 0.2f))
            listOf(1, 2, 3).forEach { count ->
                DropdownMenuItem(
                    text = { Text("$count видео в строке", color = textColor) },
                    onClick = {
                        onChangeColumns(count)
                        onMenuChange(false)
                        onDismissRequest()
                    },
                    modifier = Modifier.heightIn(min = menuItemHeight)
                )
                if (count < 3) Divider(color = textColor.copy(alpha = 0.2f))
            }
        }
    }
}




@Composable
fun TopAppBar(
    title: String,
    navigationIcon: ImageVector,
    onNavigationIconClick: () -> Unit,
    showMenu: Boolean,
    onChangeColumns: (Int) -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.primary
) {
    var showDropdown by remember { mutableStateOf(false) }
    var inColumnSettings by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = backgroundColor)
            .padding(horizontal = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = navigationIcon,
            contentDescription = "Назад",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onNavigationIconClick() }
                .padding(12.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        Box {
            if (showMenu) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Menu",
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { showDropdown = true }
                        .padding(12.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                CustomDropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    inColumnSettings = inColumnSettings,
                    onChangeColumns = onChangeColumns,
                    onMenuChange = { inColumnSettings = it }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    MaterialTheme {
        TopAppBar("Текст", Icons.Filled.Home, {}, true)
    }
}
