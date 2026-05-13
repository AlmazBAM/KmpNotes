package com.bagmanov.kmpnotes.notes_manager.domain.useCase

interface SwitchPinnedStatusUseCase {
    suspend operator fun invoke(noteId: Int)
}