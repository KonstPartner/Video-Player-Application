package com.example.videoplayerapplication.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import com.example.videoplayerapplication.screens.SortOrder

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    inColumnSettings: MutableState<Boolean>,
    inSortingOptions: MutableState<Boolean>,
    onChangeColumns: (Int) -> Unit,
    onSortChange: (SortOrder, Boolean) -> Unit
) {
    val backgroundColor = MaterialTheme.colorScheme.secondary
    val textColor = MaterialTheme.colorScheme.primary
    val menuItemHeight = 48.dp

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            if (inSortingOptions.value || inColumnSettings.value) {
                inSortingOptions.value = false
                inColumnSettings.value = false
            }
            onDismissRequest()
        },
        modifier = Modifier.width(240.dp).background(backgroundColor)
    ) {
        if (!inColumnSettings.value && !inSortingOptions.value) {
            DropdownMenuItem(
                text = { Text("Вид", color = textColor) },
                onClick = { inColumnSettings.value = true },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
            Divider(color = textColor.copy(alpha = 0.2f))
            DropdownMenuItem(
                text = { Text("Сортировать", color = textColor) },
                onClick = { inSortingOptions.value = true },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
        } else if (inSortingOptions.value) {
            DropdownMenuItem(
                text = { Text("Назад", color = textColor) },
                onClick = { inSortingOptions.value = false },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
            Divider(color = textColor.copy(alpha = 0.2f))
            SortOrder.values().forEach { sortOrder ->
                DropdownMenuItem(
                    text = { Text(sortOrder.title, color = textColor) },
                    onClick = {
                        onSortChange(sortOrder, true); inSortingOptions.value = false
                        onDismissRequest()
                    },
                    modifier = Modifier.heightIn(min = menuItemHeight)
                )
                if (sortOrder.title != "По длительности") Divider(color = textColor.copy(alpha = 0.2f))
            }
        } else if (inColumnSettings.value) {
            DropdownMenuItem(
                text = { Text("Назад", color = textColor) },
                onClick = { inColumnSettings.value = false },
                modifier = Modifier.heightIn(min = menuItemHeight)
            )
            Divider(color = textColor.copy(alpha = 0.2f))
            listOf(1, 2, 3).forEach { count ->
                DropdownMenuItem(
                    text = { Text("$count видео в строке", color = textColor) },
                    onClick = {
                        onChangeColumns(count)
                        inColumnSettings.value = false
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
    onSortChange: (SortOrder, Boolean) -> Unit = { _, _ -> },
    backgroundColor: Color = MaterialTheme.colorScheme.onSecondary,
    iconColor: Color = MaterialTheme.colorScheme.primary
) {
    var showDropdown by remember { mutableStateOf(false) }
    val inColumnSettings = remember { mutableStateOf(false) }
    val inSortingOptions = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = backgroundColor)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = navigationIcon,
            contentDescription = "Назад",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onNavigationIconClick() }
                .padding(12.dp),
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.weight(1f))
        if (showMenu) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { showDropdown = !showDropdown }
                    .padding(12.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            AnimatedVisibility(
                visible = showDropdown,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                CustomDropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false },
                    inColumnSettings = inColumnSettings,
                    inSortingOptions = inSortingOptions,
                    onChangeColumns = onChangeColumns,
                    onSortChange = onSortChange
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
