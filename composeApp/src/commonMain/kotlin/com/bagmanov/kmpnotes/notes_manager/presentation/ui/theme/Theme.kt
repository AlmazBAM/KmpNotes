package com.bagmanov.kmpnotes.notes_manager.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Brown,
    onPrimary = White,
    background = DarkGrey100,
    onBackground = LightGrey,
    surface = DarkGrey200,
    onSurface = LightGrey,
    onSurfaceVariant = Grey200,
)

private val LightColorScheme = lightColorScheme(
    primary = Brown,
    onPrimary = White,
    background = White,
    onBackground = Grey300,
    surface = Grey100,
    onSurface = Grey300,
    onSurfaceVariant = Grey200,
)

@Composable
fun NotionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
