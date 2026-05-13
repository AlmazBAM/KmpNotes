package com.bagmanov.kmpnotes.core.presentation.utils

expect object PlatformDateFormatter {
    fun formateTimeStamp(timestampMillis: Long): String
}