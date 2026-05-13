package com.bagmanov.kmpnotes.notes_manager.domain.useCase

interface DeleteNoteUseCase {
    suspend operator fun invoke(noteId: Int)
}