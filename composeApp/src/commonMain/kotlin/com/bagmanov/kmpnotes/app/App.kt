package com.bagmanov.kmpnotes.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.bagmanov.kmpnotes.notes_manager.presentation.creation.CreateNoteScreen
import com.bagmanov.kmpnotes.notes_manager.presentation.creation.CreateNoteViewModel
import com.bagmanov.kmpnotes.notes_manager.presentation.editing.EditNoteScreen
import com.bagmanov.kmpnotes.notes_manager.presentation.editing.EditNoteViewModel
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.NotesScreen
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.NotesViewModel
import com.bagmanov.kmpnotes.notes_manager.presentation.ui.theme.NotionTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    NotionTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.NoteGraph
        ) {
            navigation<Route.NoteGraph>(startDestination = Route.NoteList) {
                composable<Route.NoteList> {
                    val notesViewModel = koinViewModel<NotesViewModel>(viewModelStoreOwner = it)
                    val notesScreenState by notesViewModel.uiState.collectAsStateWithLifecycle()

                    NotesScreen(
                        uiState = notesScreenState,
                        onNoteAction = notesViewModel::onEvent,
                        onAddNote = {
                            navController.navigate(Route.CreatingNote)
                        },
                        onOpenNote = { noteId ->
                            navController.navigate(Route.EditingNote(noteId))
                        }
                    )
                }

                composable<Route.CreatingNote> {
                    val createNoteViewModel =
                        koinViewModel<CreateNoteViewModel>(viewModelStoreOwner = it)
                    val createNoteState by createNoteViewModel.uiState.collectAsStateWithLifecycle()

                    CreateNoteScreen(
                        onBackClicked = { navController.popBackStack() },
                        onCreateNoteAction = createNoteViewModel::onEvent,
                        uiState = createNoteState
                    )
                }

                composable<Route.EditingNote> {
                    val editNoteViewModel =
                        koinViewModel<EditNoteViewModel>(viewModelStoreOwner = it)

                    val editNoteState by editNoteViewModel.uiState.collectAsStateWithLifecycle()
                    val noteId = it.toRoute<Route.EditingNote>().noteId

                    LaunchedEffect(noteId) {
                        editNoteViewModel.getNoteById(noteId)
                    }

                    EditNoteScreen(
                        onBackClicked = { navController.popBackStack() },
                        onEditNoteAction = editNoteViewModel::onEvent,
                        uiState = editNoteState
                    )
                }
            }
        }
    }
}