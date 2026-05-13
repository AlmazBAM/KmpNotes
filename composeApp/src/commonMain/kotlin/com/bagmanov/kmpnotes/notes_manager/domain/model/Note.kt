package com.bagmanov.kmpnotes.notes_manager.domain.model

data class Note(
    val id: Int,
    val title: String,
    val content: List<ContentItem>,
    val updatedAt: Long,
    val isPinned: Boolean
)
