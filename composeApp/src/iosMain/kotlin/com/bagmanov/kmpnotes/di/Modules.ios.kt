package com.bagmanov.kmpnotes.di

import com.bagmanov.kmpnotes.notes_manager.data.db.DatabaseFactory
import com.bagmanov.kmpnotes.notes_manager.data.repository.ImageFileManagerImpl
import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory() }
        single<ImageFileManager> { ImageFileManagerImpl() }
//        single<ImagePicker> { ImagePickerImpl() }

    }