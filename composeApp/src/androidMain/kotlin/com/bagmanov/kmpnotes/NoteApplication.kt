package com.bagmanov.kmpnotes

import android.app.Application
import com.bagmanov.kmpnotes.di.initKoin
import org.koin.android.ext.koin.androidContext

class NoteApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@NoteApplication)
        }
    }
}