package com.bagmanov.kmpnotes.notes_manager.data.mapper

import com.bagmanov.kmpnotes.notes_manager.data.entity.ContentEntity
import com.bagmanov.kmpnotes.notes_manager.data.entity.ContentType
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteEntity
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteWithContentEntity
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = this.id,
        title = this.title,
        updatedAt = this.updatedAt,
        isPinned = this.isPinned
    )
}

fun NoteWithContentEntity.toNote(): Note {

    return Note(
        id = noteEntity.id,
        title = noteEntity.title,
        content = contentEntity.toContentItemList(),
        updatedAt = noteEntity.updatedAt,
        isPinned = noteEntity.isPinned
    )
}

fun List<NoteWithContentEntity>.toNotes(): List<Note> {
    return this.map {
        it.toNote()
    }
}

fun List<ContentItem>.toContentItemEntityList(noteId: Int): List<ContentEntity> {
    return this.mapIndexed { index, contentItem ->
        when (contentItem) {
            is ContentItem.Image -> {
                ContentEntity(
                    noteId = noteId,
                    type = ContentType.IMAGE,
                    content = contentItem.url,
                    order = index
                )
            }
            is ContentItem.Text -> {
                ContentEntity(
                    noteId = noteId,
                    type = ContentType.TEXT,
                    content = contentItem.text,
                    order = index
                )
            }
        }
    }

}

fun List<ContentEntity>.toContentItemList(): List<ContentItem> {
    return this.map { contentItem ->
        when (contentItem.type) {
            ContentType.IMAGE -> ContentItem.Image(url = contentItem.content)
            ContentType.TEXT -> ContentItem.Text(text = contentItem.content)
        }
    }

}