package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SwitchPinnedStatusUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository

class SwitchPinnedStatusInteractor(private val notesRepository: NotesRepository): SwitchPinnedStatusUseCase {

    override suspend operator fun invoke(noteId: Int) = notesRepository.switchPinnedStatus(noteId = noteId)
}