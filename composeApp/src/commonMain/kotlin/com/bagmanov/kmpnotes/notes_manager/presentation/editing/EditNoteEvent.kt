package com.bagmanov.kmpnotes.notes_manager.presentation.editing

sealed interface EditNoteEvent {
    data class InputNoteTitle(val title: String): EditNoteEvent
    data class InputNoteContent(val content: String, val index: Int): EditNoteEvent
    data class ImagePicked(val path: String): EditNoteEvent
    data class RemoveImage(val id: Int): EditNoteEvent
    data object Save: EditNoteEvent
    data object Delete: EditNoteEvent

}