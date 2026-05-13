package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SearchNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository

class SearchNotesInteractor(private val notesRepository: NotesRepository): SearchNotesUseCase {

    override operator fun invoke(query: String) = notesRepository.searchNotes(query = query)
}