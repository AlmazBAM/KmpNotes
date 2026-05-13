package com.bagmanov.kmpnotes.notes_manager.data.repository

import android.content.Context
import androidx.core.net.toUri
import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

actual class ImageFileManagerImpl(private val context: Context) : ImageFileManager {
    private val imagesDir: File = context.filesDir

    actual override suspend fun copyImagerToInternalStorage(url: String): String {
        val fileName = "IMG_${UUID.randomUUID()}.jpg"
        val file = File(imagesDir, fileName)

        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(url.toUri()).use { inputStream ->
                file.outputStream().use {
                    inputStream?.copyTo(it)
                }
            }
        }
        return file.absolutePath
    }

    actual override suspend fun deleteImage(url: String) {
        withContext(Dispatchers.IO) {
            val file = File(url)
            if (isInternal(file.absolutePath) && file.exists())
                file.delete()
        }
    }

    actual override fun isInternal(url: String): Boolean {
        return url.startsWith(imagesDir.absolutePath)
    }
}