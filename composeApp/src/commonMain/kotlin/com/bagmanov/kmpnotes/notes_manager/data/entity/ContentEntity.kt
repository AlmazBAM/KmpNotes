package com.bagmanov.kmpnotes.notes_manager.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "note_content",
    primaryKeys = ["note_id", "item_order"],
    foreignKeys = [
        ForeignKey(
            entity = NoteEntity::class,
            parentColumns = ["id"],
            childColumns = ["note_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ContentEntity(
    @ColumnInfo(name = "note_id")
    val noteId: Int,
    val type: ContentType,
    val content: String,
    @ColumnInfo(name = "item_order")
    val order: Int,
)

enum class ContentType {
    TEXT, IMAGE
}