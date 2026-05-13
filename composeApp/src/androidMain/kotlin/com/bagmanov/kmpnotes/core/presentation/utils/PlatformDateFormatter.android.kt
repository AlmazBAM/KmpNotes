package com.bagmanov.kmpnotes.core.presentation.utils

import com.bagmanov.kmpnotes.core.others.DATE_FORMAT
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

actual object PlatformDateFormatter {

    actual fun formateTimeStamp(timestampMillis: Long): String {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT).withLocale(Locale.getDefault())

        val localDateTime =
            Instant.ofEpochMilli(timestampMillis).atZone(ZoneId.systemDefault())
        return localDateTime.format(formatter)
    }
}