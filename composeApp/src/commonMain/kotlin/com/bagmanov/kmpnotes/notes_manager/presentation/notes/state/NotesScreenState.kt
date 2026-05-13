package com.bagmanov.kmpnotes.notes_manager.presentation.notes.state

import androidx.compose.runtime.Immutable
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note

@Immutable
data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = emptyList(),
    val otherNotes: List<Note> = emptyList()
)