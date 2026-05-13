package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.DeleteNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository

class DeleteNoteInteractor(private val notesRepository: NotesRepository): DeleteNoteUseCase {

     override suspend operator fun invoke(noteId: Int) = notesRepository.deleteNote(noteId = noteId)
}