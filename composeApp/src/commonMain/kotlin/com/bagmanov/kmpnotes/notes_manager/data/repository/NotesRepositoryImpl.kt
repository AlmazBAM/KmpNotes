package com.bagmanov.kmpnotes.notes_manager.data.repository

import com.bagmanov.kmpnotes.notes_manager.data.db.NotesDao
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteEntity
import com.bagmanov.kmpnotes.notes_manager.data.mapper.toContentItemEntityList
import com.bagmanov.kmpnotes.notes_manager.data.mapper.toEntity
import com.bagmanov.kmpnotes.notes_manager.data.mapper.toNote
import com.bagmanov.kmpnotes.notes_manager.data.mapper.toNotes
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.domain.model.Note
import com.bagmanov.kmpnotes.notes_manager.domain.repository.ImageFileManager
import com.bagmanov.kmpnotes.notes_manager.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val dao: NotesDao,
    private val imageFileManager: ImageFileManager,
) : NotesRepository {

    override suspend fun addNote(
        title: String,
        content: List<ContentItem>,
        isPinned: Boolean,
        updateAt: Long,
    ) {
        val processedContent = content.processForStorage()
        dao.addNoteWithContent(NoteEntity(0, title, updateAt, isPinned), processedContent)
    }

    override suspend fun deleteNote(noteId: Int) {
        val note = dao.getNote(noteId).toNote()
        dao.deleteNote(noteId)

        note.content.filterIsInstance<ContentItem.Image>().forEach {
            imageFileManager.deleteImage(it.url)
        }
    }

    override suspend fun editNote(note: Note) {
        val oldNote = dao.getNote(note.id).toNote()

        val oldUrls = oldNote.content.filterIsInstance<ContentItem.Image>().map { it.url }
        val newUrls = note.content.filterIsInstance<ContentItem.Image>().map { it.url }.toSet()
        val removedUrls = oldUrls - newUrls
        removedUrls.forEach {
            imageFileManager.deleteImage(it)
        }
        val processedContent = note.content.processForStorage()
        val processedNote = note.copy(
            content = processedContent
        )

        dao.editNoteWithContent(
            processedNote.toEntity(),
            processedContent.toContentItemEntityList(note.id)
        )
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { notes -> notes.toNotes() }
    }

    override suspend fun getNote(noteId: Int): Note {
        return dao.getNote(noteId).toNote()
    }

    override fun searchNotes(query: String): Flow<List<Note>> {
        return dao.searchNotes(query).map { notes -> notes.toNotes() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        dao.switchPinnedStatus(noteId)
    }

    private suspend fun List<ContentItem>.processForStorage(): List<ContentItem> {
        return map { contentItem ->
            when (contentItem) {
                is ContentItem.Image -> {
                    if (imageFileManager.isInternal(contentItem.url)) {
                        contentItem
                    } else {
                        val internalPath =
                            imageFileManager.copyImagerToInternalStorage(contentItem.url)
                        ContentItem.Image(internalPath)
                    }

                }

                is ContentItem.Text -> contentItem
            }
        }
    }
}