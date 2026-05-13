package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository

class GetNoteInteractor(private val notesRepository: NotesRepository): GetNoteUseCase {

    override suspend operator fun invoke(noteId: Int) = notesRepository.getNote(noteId = noteId)
}