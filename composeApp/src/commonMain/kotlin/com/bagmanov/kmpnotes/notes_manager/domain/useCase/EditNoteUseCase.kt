package com.bagmanov.kmpnotes.notes_manager.domain.useCase

import com.bagmanov.kmpnotes.notes_manager.domain.model.Note

interface EditNoteUseCase {
    suspend operator fun invoke(note: Note)
}