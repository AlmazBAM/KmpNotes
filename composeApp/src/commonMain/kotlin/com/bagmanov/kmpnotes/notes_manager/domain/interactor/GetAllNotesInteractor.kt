package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetAllNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class GetAllNotesInteractor(private val notesRepository: NotesRepository): GetAllNotesUseCase {

     override operator fun invoke(): Flow<List<Note>> = notesRepository.getAllNotes()
}