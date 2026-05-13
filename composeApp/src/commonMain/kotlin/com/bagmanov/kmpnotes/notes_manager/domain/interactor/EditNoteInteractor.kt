package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.EditNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository
import kotlinx.datetime.Clock

class EditNoteInteractor(private val notesRepository: NotesRepository): EditNoteUseCase {

    override suspend operator fun invoke(note: Note) = notesRepository.editNote(
        note = note.copy(
            updatedAt = Clock.System.now().toEpochMilliseconds()
        )
    )
}