package com.bagmanov.kmpnotes

import androidx.compose.ui.window.ComposeUIViewController
import com.bagmanov.kmpnotes.app.App
import com.bagmanov.kmpnotes.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }