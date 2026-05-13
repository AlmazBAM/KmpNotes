package com.bagmanov.kmpnotes.core.presentation.utils

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberImagePickerLauncher(onResult: (String?) -> Unit): ImagePickerLauncher {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onResult(uri?.toString())
    }
    return remember {
        object : ImagePickerLauncher {
            override fun launch() {
                launcher.launch("image/*")
            }
        }
    }
}