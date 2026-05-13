package com.bagmanov.kmpnotes.notes_manager.data.db

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<NotesDatabase>
}