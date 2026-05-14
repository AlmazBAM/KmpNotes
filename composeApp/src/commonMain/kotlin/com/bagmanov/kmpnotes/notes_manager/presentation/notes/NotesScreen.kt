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
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagmanov.kmpnotes.core.presentation.NoteCard
import com.bagmanov.kmpnotes.core.presentation.NoteCardWithImage
import com.bagmanov.kmpnotes.core.presentation.SearchBar
import com.bagmanov.kmpnotes.core.presentation.Subtitle
import com.bagmanov.kmpnotes.core.presentation.Title
import com.bagmanov.kmpnotes.core.presentation.ui.theme.OtherNotesColors
import com.bagmanov.kmpnotes.core.presentation.ui.theme.PinnedNotesColors
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem
import com.bagmanov.kmpnotes.notes_manager.presentation.notes.state.NotesScreenState
import kmpnotes.composeapp.generated.resources.Res
import kmpnotes.composeapp.generated.resources.all_notes
import kmpnotes.composeapp.generated.resources.favorites_tab
import kmpnotes.composeapp.generated.resources.home_tab
import kmpnotes.composeapp.generated.resources.others
import kmpnotes.composeapp.generated.resources.pinned
import kmpnotes.composeapp.generated.resources.search_tab
import kmpnotes.composeapp.generated.resources.settings_tab
import org.jetbrains.compose.resources.StringResource
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
    val primary = MaterialTheme.colorScheme.primary
    val unselected = MaterialTheme.colorScheme.onSurface

    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar(
                containerColor = Color.Transparent
            ) {
                val left = listOf(Tab.Home, Tab.Favourites)
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
                        note.content.filterIsInstance<ContentItem.Image>().map { it.url }
                            .firstOrNull()

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
    onClick: (Tab) -> Unit,
) {
    val baseStyle = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.SemiBold
    )
    NavigationBarItem(
        selected = selected == item,
        onClick = { onClick(item) },
        icon = { Icon(item.icon, contentDescription = null) },
        label = {
            if (selected == item) {
                BasicText(
                    text = stringResource(item.label),
                    style = baseStyle,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 8.sp,
                        maxFontSize = baseStyle.fontSize,
                        stepSize = 0.5.sp
                    )
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = primary,
            selectedTextColor = primary,
            indicatorColor = Color.Transparent,
            unselectedIconColor = unselected,
            unselectedTextColor = unselected
        )
    )
}


enum class Tab(val label: StringResource, val icon: ImageVector) {
    Home(Res.string.home_tab, Icons.Outlined.Home),
    Favourites(Res.string.favorites_tab, Icons.Outlined.Star),
    Search(Res.string.search_tab, Icons.Outlined.Search),
    Settings(Res.string.settings_tab, Icons.Outlined.Settings)
}
