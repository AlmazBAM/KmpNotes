package com.bagmanov.kmpnotes.notes_manager.presentation.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.AddNoteUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.presentation.creation.state.CreateNoteState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateNoteViewModel(
    val addNoteInteractor: AddNoteUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(CreateNoteState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        CreateNoteState()
    )

    fun onEvent(createNoteEvent: CreateNoteEvent) {
        when (createNoteEvent) {
            is CreateNoteEvent.InputNoteContent -> {
                _uiState.update { state ->
                    val newContent = state.content
                        .mapIndexed { index, contentItem ->
                            if (index == createNoteEvent.index && contentItem is ContentItem.Text) {
                                contentItem.copy(
                                    text = createNoteEvent.content
                                )
                            } else {
                                contentItem
                            }
                        }
                    state.copy(
                        content = newContent
                    )
                }
            }

            is CreateNoteEvent.InputNoteTitle -> {
                _uiState.update { state ->
                    state.copy(
                        title = createNoteEvent.title
                    )
                }
            }

            CreateNoteEvent.Save -> {
                viewModelScope.launch {
                    val title = _uiState.value.title
                    val content = _uiState.value.content.filter {
                        it !is ContentItem.Text || it.text.isNotBlank()
                    }
                    addNoteInteractor(
                        title = title,
                        content = content
                    )
                }
                _uiState.update {
                    it.copy(
                        title = "",
                        content = listOf(ContentItem.Text(""))
                    )
                }
            }

            is CreateNoteEvent.ImagePicked -> {
                _uiState.update { state ->
                    state.content.toMutableList().apply {
                        val lastItem = last()
                        if (lastItem is ContentItem.Text && lastItem.text.isBlank())
                            removeAt(lastIndex)
                        add(ContentItem.Image(createNoteEvent.path))
                        add(ContentItem.Text(""))
                    }.let {
                        state.copy(
                            content = it
                        )
                    }
                }
            }

            is CreateNoteEvent.RemoveImage -> {
                _uiState.update { state ->
                    state.content.toMutableList().apply {
                        removeAt(createNoteEvent.id)
                    }.let {
                        state.copy(
                            content = it
                        )
                    }

                }
            }
        }
    }

}