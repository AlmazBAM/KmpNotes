package com.bagmanov.kmpnotes.notes_manager.presentation.notes


sealed interface NotesEvent {
    data class SearchQueryInput(val query: String): NotesEvent
    data class ChangePinnedStatus(val noteId: Int): NotesEvent
}