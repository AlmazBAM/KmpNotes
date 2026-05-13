package com.bagmanov.kmpnotes.notes_manager.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.GetAllNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SearchNotesUseCase
import com.bagmanov.kmpnotes.notes_manager.domain.useCase.SwitchPinnedStatusUseCase
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.state.NotesScreenState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val getAllNotesInteractor: GetAllNotesUseCase,
    private val switchPinnedStatusInteractor: SwitchPinnedStatusUseCase,
    private val searchNotesInteractor: SearchNotesUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(NotesScreenState())
    val uiState = _uiState
        .onStart { registerOnQueryChange() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            NotesScreenState()
        )

    private val searchQueryFlow = MutableStateFlow("")

    fun onEvent(notesEvent: NotesEvent) {
        when (notesEvent) {
            is NotesEvent.ChangePinnedStatus -> {
                viewModelScope.launch {
                    switchPinnedStatusInteractor(notesEvent.noteId)
                }
            }

            is NotesEvent.SearchQueryInput -> {
                searchQueryFlow.update { notesEvent.query.trim() }
            }
        }
    }

    private fun registerOnQueryChange() {
        searchQueryFlow
            .onEach { input ->
                _uiState.update { it.copy(query = input) }
            }
            .flatMapLatest {  query ->
                if (query.isBlank())
                    getAllNotesInteractor()
                else
                    searchNotesInteractor(query)
            }
            .onEach { notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter { !it.isPinned }
                _uiState.update {
                    it.copy(
                        pinnedNotes = pinnedNotes,
                        otherNotes = otherNotes
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}