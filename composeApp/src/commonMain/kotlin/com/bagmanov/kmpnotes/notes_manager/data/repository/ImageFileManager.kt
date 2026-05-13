package com.bagmanov.kmpnotes.notes_manager.data.repository

import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager

expect class ImageFileManagerImpl : ImageFileManager {
    override suspend fun copyImagerToInternalStorage(url: String): String
    override suspend fun deleteImage(url: String)
    override fun isInternal(url: String): Boolean
}