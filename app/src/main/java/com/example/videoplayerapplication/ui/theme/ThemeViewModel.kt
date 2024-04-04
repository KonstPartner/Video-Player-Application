package com.example.videoplayerapplication.ui.theme

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf

class ThemeViewModel : ViewModel() {
    val isDarkTheme = mutableStateOf(false)

    fun toggleTheme() {
        isDarkTheme.value = !isDarkTheme.value
    }
}

