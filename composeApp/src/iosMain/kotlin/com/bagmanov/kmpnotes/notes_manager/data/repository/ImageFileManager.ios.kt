package com.bagmanov.kmpnotes.notes_manager.data.repository

import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSApplicationSupportDirectory
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSUUID

@OptIn(ExperimentalForeignApi::class)
actual class ImageFileManagerImpl : ImageFileManager {
    
    private val fileManager = NSFileManager.defaultManager
    private val appSupportDirectory: NSURL by lazy {
        val urls = fileManager.URLsForDirectory(NSApplicationSupportDirectory, NSUserDomainMask)
        val base = urls.firstOrNull() as NSURL? ?: error("Application Support directory not found")
        val dir = base.URLByAppendingPathComponent("NotesImages")!!
        if (!fileManager.fileExistsAtPath(dir.path!!)) {
            fileManager.createDirectoryAtURL(
                dir,
                withIntermediateDirectories = true,
                attributes = null,
                error = null
            )
        }
        dir
    }

    actual override suspend fun copyImagerToInternalStorage(url: String): String {
        val sourceURL = if (url.startsWith("file://")) {
            NSURL.URLWithString(url) ?: return url
        } else {
            NSURL.fileURLWithPath(url)
        }
        val fileName = "IMG_${NSUUID().UUIDString()}.jpg"
        val destinationURL = appSupportDirectory.URLByAppendingPathComponent(fileName)!!
        
        return if (fileManager.copyItemAtURL(sourceURL, destinationURL, null)) {
            destinationURL.path!!
        } else {
            url
        }
    }

    actual override suspend fun deleteImage(url: String) {
        val fileURL = when {
            url.startsWith("file://") -> NSURL.URLWithString(url)
            else -> NSURL.fileURLWithPath(url)
        } ?: return
        val path = fileURL.path ?: return
        if (fileManager.fileExistsAtPath(path)) {
            fileManager.removeItemAtURL(fileURL, null)
        }
    }

    actual override fun isInternal(url: String): Boolean {
        val path = when {
            url.startsWith("file://") -> NSURL.URLWithString(url)?.path
            else -> url
        } ?: return false
        return path.startsWith(appSupportDirectory.path!!) ?: false
    }
}
