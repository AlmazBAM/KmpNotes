package com.bagmanov.kmpnotes.notes_manager.data.entity

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithContentEntity(
    @Embedded
    val noteEntity: NoteEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "note_id"
    )
    val contentEntity: List<ContentEntity>,
)
