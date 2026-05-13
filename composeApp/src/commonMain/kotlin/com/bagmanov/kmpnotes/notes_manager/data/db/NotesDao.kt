package com.bagmanov.kmpnotes.notes_manager.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.bagmanov.kmpnotes.notes_manager.data.entity.ContentEntity
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteEntity
import com.bagmanov.kmpnotes.notes_manager.data.entity.NoteWithContentEntity
import com.bagmanov.kmpnotes.notes_manager.data.mapper.toContentItemEntityList
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert(entity = NoteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(note: NoteEntity): Long

    @Insert(entity = ContentEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNoteContent(content: List<ContentEntity>)

    @Transaction
    @Query("DELETE FROM notes WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("DELETE FROM note_content WHERE note_id=:noteId")
    suspend fun deleteContent(noteId: Int)

    @Transaction
    @Query("SELECT * FROM notes WHERE  id=:noteId")
    suspend fun getNote(noteId: Int): NoteWithContentEntity

    @Transaction
    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    fun getAllNotes(): Flow<List<NoteWithContentEntity>>

    @Query("UPDATE notes SET is_pinned= NOT is_pinned WHERE id=:noteId")
    suspend fun switchPinnedStatus(noteId: Int)

    @Transaction
    @Query(
        """
        SELECT DISTINCT notes.* FROM notes JOIN note_content 
        ON notes.id == note_content.note_id 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%' 
        ORDER BY updated_at DESC
    """
    )
    fun searchNotes(query: String): Flow<List<NoteWithContentEntity>>

    @Transaction
    suspend fun addNoteWithContent(
        noteEntity: NoteEntity,
        contents: List<ContentItem>
    ) {
        val noteId = addNote(noteEntity).toInt()
        val contentEntities = contents.toContentItemEntityList(noteId)
        addNoteContent(contentEntities)
    }

    @Transaction
    suspend fun editNoteWithContent(
        noteEntity: NoteEntity,
        contents: List<ContentEntity>
    ) {
        addNote(noteEntity)
        deleteContent(noteEntity.id)
        addNoteContent(contents)
    }


}