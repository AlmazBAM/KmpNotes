package com.bagmanov.kmpnotes.notes_manager.data.repository

import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager

actual class ImageFileManagerImpl : ImageFileManager {
    actual override suspend fun copyImagerToInternalStorage(url: String): String {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteImage(url: String) {
        TODO("Not yet implemented")
    }

    actual override fun isInternal(url: String): Boolean {
        TODO("Not yet implemented")
    }
}