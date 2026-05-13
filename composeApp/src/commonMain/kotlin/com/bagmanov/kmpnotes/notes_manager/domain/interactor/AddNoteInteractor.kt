package com.bagmanov.kmpnotes.notes_manager.domain.interactor

import com.bagmanov.kmpnotes.notes_manager.domain.useCase.AddNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository
import kotlinx.datetime.Clock

class AddNoteInteractor(private val notesRepository: NotesRepository): AddNoteUseCase {

    override suspend operator fun invoke(title: String, content: List<ContentItem>) =
        notesRepository.addNote(
            title = title,
            content = content,
            isPinned = false,
            updateAt = Clock.System.now().toEpochMilliseconds()
        )
}