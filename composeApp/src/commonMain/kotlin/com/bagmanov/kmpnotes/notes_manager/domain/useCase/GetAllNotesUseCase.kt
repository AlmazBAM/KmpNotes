package com.bagmanov.kmpnotes.notes_manager.domain.useCase

import com.bagmanov.kmpnotes.notes_manager.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface GetAllNotesUseCase {
    operator fun invoke(): Flow<List<Note>>
}