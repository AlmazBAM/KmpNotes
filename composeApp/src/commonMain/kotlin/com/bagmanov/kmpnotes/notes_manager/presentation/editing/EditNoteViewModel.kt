package com.bagmanov.kmpnotes.notes_manager.presentation.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.DeleteNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.EditNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.presentation.editing.state.EditNoteState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditNoteViewModel(
    val editNoteInteractor: EditNoteUseCase,
    val getNoteInteractor: GetNoteUseCase,
    val deleteNoteInteractor: DeleteNoteUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow<EditNoteState>(EditNoteState.Loading)
    val uiState = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            EditNoteState.Loading
        )

    fun onEvent(editNoteEvent: EditNoteEvent) {
        when (editNoteEvent) {
            is EditNoteEvent.InputNoteContent -> {
                _uiState.update { state ->
                    if (state is EditNoteState.Editing) {
                        state.note.content
                            .mapIndexed { index, content ->
                                if (index == editNoteEvent.index && content is ContentItem.Text) {
                                    content.copy(
                                        text = editNoteEvent.content
                                    )
                                } else {
                                    content
                                }
                            }.let {
                                val newNote = state.note.copy(
                                    content = it
                                )
                                state.copy(
                                    note = newNote
                                )
                            }
                    } else
                        state
                }
            }

            is EditNoteEvent.InputNoteTitle -> {
                _uiState.update { state ->
                    if (state is EditNoteState.Editing) {
                        val newNote = state.note.copy(
                            title = editNoteEvent.title
                        )
                        state.copy(
                            note = newNote
                        )
                    } else
                        state
                }
            }

            EditNoteEvent.Save -> {
                viewModelScope.launch {
                    _uiState.update { state ->
                        if (state is EditNoteState.Editing) {
                            val note = state.note
                            val content = note.content.filter {
                                it !is ContentItem.Text || it.text.isNotBlank()
                            }
                            editNoteInteractor(note.copy(content = content))
                            EditNoteState.Finished
                        } else
                            state
                    }
                }
            }

            EditNoteEvent.Delete -> {
                viewModelScope.launch {
                    _uiState.update { state ->
                        if (state is EditNoteState.Editing) {
                            deleteNoteInteractor(state.note.id)
                            EditNoteState.Finished
                        } else
                            state
                    }
                }
            }

            is EditNoteEvent.RemoveImage -> {
                _uiState.update { state ->
                    if (state is EditNoteState.Editing) {
                        val initialNode = state.note
                        initialNode.content.toMutableList().apply {
                            removeAt(editNoteEvent.id)
                        }.let {
                            val newNote = initialNode.copy(
                                content = it
                            )
                            state.copy(
                                note = newNote
                            )
                        }
                    } else {
                        state
                    }

                }
            }

            is EditNoteEvent.ImagePicked -> {
                _uiState.update { state ->
                    if (state is EditNoteState.Editing) {
                        state.note.content.toMutableList().apply {
                            val lastItem = last()
                            if (lastItem is ContentItem.Text && lastItem.text.isBlank()) {
                                removeAt(lastIndex)
                            }

                            add(ContentItem.Image(editNoteEvent.path))

                            add(ContentItem.Text(""))
                        }.let {
                            val newNote = state.note.copy(
                                content = it
                            )
                            state.copy(
                                note = newNote
                            )
                        }
                    } else {
                        state
                    }
                }
            }
        }
    }

    fun getNoteById(noteId: Int) {
        viewModelScope.launch {
            val note = getNoteInteractor(noteId).let { note ->
                val content = note.content.last()
                if (content !is ContentItem.Text) {
                    note.copy(
                        content = note.content + ContentItem.Text("")
                    )
                } else {
                    note
                }
            }

            _uiState.update {
                EditNoteState.Editing(note)
            }
        }
    }
}