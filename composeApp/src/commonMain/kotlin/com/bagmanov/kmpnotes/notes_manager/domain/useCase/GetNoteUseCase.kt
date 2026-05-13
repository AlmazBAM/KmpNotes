package com.bagmanov.kmpnotes.notes_manager.domain.useCase

import com.bagmanov.kmpnotes.notes_manager.domain.model.Note

interface GetNoteUseCase {
    suspend operator fun invoke(noteId: Int): Note
}