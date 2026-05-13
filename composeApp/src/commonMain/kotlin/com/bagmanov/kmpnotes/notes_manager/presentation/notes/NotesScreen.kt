package com.bagmanov.kmpnotes.notes_manager.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.unit.dp
import com.bagmanov.kmpnotes.core.presentation.NoteCard
import com.bagmanov.kmpnotes.core.presentation.NoteCardWithImage
import com.bagmanov.kmpnotes.core.presentation.SearchBar
import com.bagmanov.kmpnotes.core.presentation.Subtitle
import com.bagmanov.kmpnotes.core.presentation.Title
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.state.NotesScreenState
import com.bagmanov.kmpnotes.notes_manager.presentation.ui.theme.OtherNotesColors
import com.bagmanov.kmpnotes.notes_manager.presentation.ui.theme.PinnedNotesColors
import kmpnotes.composeapp.generated.resources.Res
import kmpnotes.composeapp.generated.resources.all_notes
import kmpnotes.composeapp.generated.resources.others
import kmpnotes.composeapp.generated.resources.pinned
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    uiState: NotesScreenState,
    onNoteAction: (NotesEvent) -> Unit,
    onAddNote: () -> Unit,
    onOpenNote: (Int) -> Unit,
) {
    var selected by remember { mutableStateOf(Tab.Home) }
    val primary = Color(0xFF6A40C4)      // фиолетовый из примера
    val unselected = Color(0xFF8E8E93)   // серый

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                val left = listOf(Tab.Home, Tab.Finished)
                val right = listOf(Tab.Search, Tab.Settings)

                left.forEach { item ->
                    NavItem(item, selected, primary, unselected) { selected = it }
                }
                Spacer(Modifier.weight(1f))
                right.forEach { item ->
                    NavItem(item, selected, primary, unselected) { selected = it }
                }

            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .size(64.dp)
                    .offset(y = 40.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp),
                onClick = onAddNote,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Button add note"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            Title(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = stringResource(Res.string.all_notes)
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                modifier = Modifier.padding(horizontal = 24.dp),
                query = uiState.query,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = true,
                    showKeyboardOnFocus = true,
                    hintLocales = LocaleList(Locale("ru"))
                ),
                onQueryChange = { onNoteAction(NotesEvent.SearchQueryInput(it)) }
            )
            LazyColumn(
                modifier = modifier.fillMaxSize(),
            ) {


                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = modifier.padding(horizontal = 24.dp),
                        text = stringResource(Res.string.pinned)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 24.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.pinnedNotes,
                            key = { _, note -> note.id }
                        ) { index, note ->
                            NoteCard(
                                modifier = modifier.widthIn(max = 160.dp),
                                note = note,
                                backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size],
                                onClick = {
                                    onOpenNote(note.id)
                                },
                                onLongClick = {
                                    onNoteAction(NotesEvent.ChangePinnedStatus(note.id))
                                }
                            )
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                item {
                    Subtitle(
                        modifier = modifier.padding(horizontal = 24.dp),
                        text = stringResource(Res.string.others)
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                itemsIndexed(
                    items = uiState.otherNotes,
                    key = { _, note -> note.id }
                ) { index, note ->
                    val firstImageUrl =
                        note.content.filterIsInstance<ContentItem.Image>().map { it.url }.firstOrNull()

                    if (firstImageUrl == null) {
                        NoteCard(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            note = note,
                            backgroundColor = OtherNotesColors[index % OtherNotesColors.size],
                            onClick = {
                                onOpenNote(note.id)
                            },
                            onLongClick = {
                                onNoteAction(NotesEvent.ChangePinnedStatus(note.id))
                            }
                        )
                    } else {
                        NoteCardWithImage(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            note = note,
                            imageUrl = firstImageUrl,
                            backgroundColor = OtherNotesColors[index % OtherNotesColors.size],
                            onClick = {
                                onOpenNote(note.id)
                            },
                            onLongClick = {
                                onNoteAction(NotesEvent.ChangePinnedStatus(note.id))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(
    item: Tab,
    selected: Tab,
    primary: Color,
    unselected: Color,
    onClick: (Tab) -> Unit
) {
    NavigationBarItem(
        selected = selected == item,
        onClick = { onClick(item) },
        icon = { Icon(item.icon, contentDescription = null) },
        label = { if (selected == item) Text(item.label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = primary,
            selectedTextColor = primary,
            indicatorColor = Color.Transparent, // без плашки под иконкой
            unselectedIconColor = unselected,
            unselectedTextColor = unselected
        )
    )
}


enum class Tab(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    Finished("Finished", Icons.Outlined.Star),
    Search("Search", Icons.Outlined.Search),
    Settings("Settings", Icons.Outlined.Settings)
}
