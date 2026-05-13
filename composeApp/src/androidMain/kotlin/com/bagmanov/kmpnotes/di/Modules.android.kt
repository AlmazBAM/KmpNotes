package com.bagmanov.kmpnotes.di

import com.bagmanov.kmpnotes.notes_manager.data.db.DatabaseFactory
import com.bagmanov.kmpnotes.notes_manager.data.repository.ImageFileManagerImpl
import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { DatabaseFactory(androidApplication()) }
        single<ImageFileManager> { ImageFileManagerImpl(get()) }
//        single<ImagePicker> { ImagePickerImpl() }

    }