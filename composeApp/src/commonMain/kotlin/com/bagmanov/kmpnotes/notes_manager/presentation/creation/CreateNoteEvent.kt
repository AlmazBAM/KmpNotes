package com.bagmanov.kmpnotes.notes_manager.presentation.creation

sealed interface CreateNoteEvent {
    data class InputNoteTitle(val title: String): CreateNoteEvent
    data class InputNoteContent(val content: String, val index: Int): CreateNoteEvent
    data class RemoveImage(val id: Int): CreateNoteEvent
    data class ImagePicked(val path: String): CreateNoteEvent
    data object Save: CreateNoteEvent
}