package com.bagmanov.kmpnotes.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object NoteGraph : Route

    @Serializable
    object NoteList : Route

    @Serializable
    object CreatingNote : Route

    @Serializable
    data class EditingNote(val noteId: Int) : Route
}