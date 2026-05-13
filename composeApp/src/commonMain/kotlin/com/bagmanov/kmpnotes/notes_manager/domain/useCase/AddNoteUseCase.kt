package com.bagmanov.kmpnotes.notes_manager.domain.useCase

import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem

interface AddNoteUseCase {
    suspend operator fun invoke(title: String, content: List<ContentItem>)
}