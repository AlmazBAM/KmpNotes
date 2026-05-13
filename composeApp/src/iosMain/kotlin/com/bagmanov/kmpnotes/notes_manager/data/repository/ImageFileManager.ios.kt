package com.bagmanov.kmpnotes.notes_manager.data.repository

import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.NSUUID

actual class ImageFileManagerImpl : ImageFileManager {
    
    private val fileManager = NSFileManager.defaultManager
    private val documentsDirectory = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).first() as NSURL

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun copyImagerToInternalStorage(url: String): String {
        val sourceURL = if (url.startsWith("file://")) NSURL.URLWithString(url)!! else NSURL.fileURLWithPath(url)
        val fileName = "IMG_${NSUUID().UUIDString()}.jpg"
        val destinationURL = documentsDirectory.URLByAppendingPathComponent(fileName)!!
        
        if (fileManager.copyItemAtURL(sourceURL, destinationURL, null)) {
            return destinationURL.absoluteString!!
        } else {
            return url
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    actual override suspend fun deleteImage(url: String) {
        val fileURL = if (url.startsWith("file://")) NSURL.URLWithString(url)!! else NSURL.fileURLWithPath(url)
        if (fileManager.fileExistsAtPath(fileURL.path!!)) {
            fileManager.removeItemAtURL(fileURL, null)
        }
    }

    actual override fun isInternal(url: String): Boolean {
        val path = if (url.startsWith("file://")) NSURL.URLWithString(url)?.path else url
        return path?.startsWith(documentsDirectory.path!!) ?: false
    }
}
