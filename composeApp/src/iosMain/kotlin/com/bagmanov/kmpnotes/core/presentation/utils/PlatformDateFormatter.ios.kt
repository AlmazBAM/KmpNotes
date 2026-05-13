package com.bagmanov.kmpnotes.core.presentation.utils

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSDateFormatterMediumStyle
import platform.Foundation.NSLocale
import platform.Foundation.NSTimeZone
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.systemTimeZone

actual object PlatformDateFormatter {

    private val formatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterMediumStyle
        locale = NSLocale.currentLocale()
        timeZone = NSTimeZone.systemTimeZone()
    }

    actual fun formateTimeStamp(timestampMillis: Long): String {
        val nsDate = NSDate.dateWithTimeIntervalSince1970(timestampMillis / 1000.0)
        return formatter.stringFromDate(nsDate)
    }
}