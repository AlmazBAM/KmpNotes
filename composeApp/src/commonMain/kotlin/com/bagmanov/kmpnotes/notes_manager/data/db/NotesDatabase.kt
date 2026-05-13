package com.bagmanov.kmpnotes.notes_manager.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bagmanov.kmpnotes.notes_manager.data.db.NotesDao
import com.bagmanov.kmpnotes.notes_manager.data.entity.ContentEntity
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteEntity
import com.bagmanov.kmpnotes.notes_manager.data.db.Converters

@Database(
    entities = [
        NoteEntity::class,
        ContentEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
@ConstructedBy(NotesDatabaseConstructor::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NotesDao

    companion object {
        const val DB_NAME = "notes.db"
    }
}