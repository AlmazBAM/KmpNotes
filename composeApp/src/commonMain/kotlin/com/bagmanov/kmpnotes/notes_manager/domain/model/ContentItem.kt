package com.bagmanov.kmpnotes.notes_manager.domain.model


sealed interface ContentItem {
    data class Text(val text: String): ContentItem
    data class Image(val url: String): ContentItem
}