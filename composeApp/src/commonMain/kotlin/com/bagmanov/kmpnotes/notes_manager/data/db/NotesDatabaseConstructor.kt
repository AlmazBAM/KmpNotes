package com.bagmanov.kmpnotes.notes_manager.data.db

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object NotesDatabaseConstructor: RoomDatabaseConstructor<NotesDatabase> {
    override fun initialize(): NotesDatabase
}