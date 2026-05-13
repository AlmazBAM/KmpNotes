package com.bagmanov.kmpnotes.notes_manager.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<NotesDatabase> {
        val dbFile = context.getDatabasePath(NotesDatabase.DB_NAME)
        return Room.databaseBuilder(context = context.applicationContext, name = dbFile.absolutePath)
    }
}