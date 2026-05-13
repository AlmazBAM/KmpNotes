package com.bagmanov.kmpnotes.core.presentation.utils

import androidx.compose.runtime.Composable
import kmpnotes.composeapp.generated.resources.Res
import kmpnotes.composeapp.generated.resources.hour_ago
import kmpnotes.composeapp.generated.resources.houra_ago
import kmpnotes.composeapp.generated.resources.hours_ago
import kmpnotes.composeapp.generated.resources.just_now
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
object DateFormatter {

    @Composable
    fun formatDateToString(timeStamp: Long): String {
        val now = Clock.System.now()
        val target = Instant.fromEpochMilliseconds(timeStamp)
        val diffMillis = now.toEpochMilliseconds() - target.toEpochMilliseconds()
        if (diffMillis < 0) return PlatformDateFormatter.formateTimeStamp(timeStamp)
        val minutes = diffMillis / 60_000
        val hours = diffMillis / 3_600_000
        return when {
            minutes < 60 -> stringResource(Res.string.just_now)
            hours < 24 -> formatHours(hours)
            else -> PlatformDateFormatter.formateTimeStamp(timeStamp)
        }
    }

    fun formatCurrentDate(): String {
        return PlatformDateFormatter.formateTimeStamp(Clock.System.now().toEpochMilliseconds())
    }

    @Composable
    private fun formatHours(hours: Long): String {
        return when {
            hours % 10 == 1L && hours % 100 != 11L -> stringResource(Res.string.hour_ago, hours)
            hours % 10 in 2..4 && hours % 100 !in 12..14 -> stringResource(
                Res.string.houra_ago,
                hours
            )

            else -> stringResource(Res.string.hours_ago, hours)
        }
    }
}