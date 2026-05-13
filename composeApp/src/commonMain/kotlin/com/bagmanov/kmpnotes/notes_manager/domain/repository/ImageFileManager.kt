package com.bagmanov.kmpnotes.notes_manager.domain.repository

interface ImageFileManager {

    suspend fun copyImagerToInternalStorage(url: String): String

    suspend fun deleteImage(url: String)

    fun isInternal(url: String): Boolean
}