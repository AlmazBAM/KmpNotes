package com.bagmanov.kmpnotes.core.presentation.utils

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(onResult: (String?) -> Unit): ImagePickerLauncher

interface ImagePickerLauncher {
    fun launch()
}