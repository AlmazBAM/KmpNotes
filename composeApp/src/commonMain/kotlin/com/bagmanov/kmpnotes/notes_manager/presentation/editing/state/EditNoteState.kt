package com.bagmanov.kmpnotes.notes_manager.presentation.editing.state

import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note

sealed interface EditNoteState {

    data object Loading : EditNoteState
    data class Editing(
        val note: Note,
    ) : EditNoteState {
        val isSaveEnabled: Boolean
            get() {
                return when {
                    note.title.isBlank() -> false
                    note.content.isEmpty() -> false
                    else -> {
                        note.content.any {
                            it !is ContentItem.Text || it.text.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : EditNoteState
}
