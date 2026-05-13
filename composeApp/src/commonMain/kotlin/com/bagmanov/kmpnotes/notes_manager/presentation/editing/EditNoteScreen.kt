package com.bagmanov.kmpnotes.notes_manager.presentation.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagmanov.kmpnotes.core.presentation.Content
import com.bagmanov.kmpnotes.core.presentation.utils.DateFormatter
import com.bagmanov.kmpnotes.notes_manager.presentation.editing.state.EditNoteState
import com.bagmanov.kmpnotes.notes_manager.presentation.ui.icons.CustomIcons
import com.bagmanov.kmpnotes.core.presentation.utils.rememberImagePickerLauncher
import kmpnotes.composeapp.generated.resources.Res
import kmpnotes.composeapp.generated.resources.edit_note
import kmpnotes.composeapp.generated.resources.save_note
import kmpnotes.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNoteScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onEditNoteAction: (EditNoteEvent) -> Unit,
    uiState: EditNoteState,
) {

    val imagePickerLauncher = rememberImagePickerLauncher { path ->
        path?.let {
            onEditNoteAction(EditNoteEvent.ImagePicked(it))
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.edit_note),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                            .clickable {
                                imagePickerLauncher.launch()
                            },
                        imageVector = CustomIcons.AddPhoto,
                        contentDescription = "Add image from gallery"
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 1.dp)
                            .clickable {
                                onEditNoteAction(EditNoteEvent.Delete)
                            },
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Close edit note screen"
                    )
                },
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .clickable {
                                onBackClicked()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close edit note screen"
                    )
                }
            )
        }) { paddingValues ->
        when (uiState) {
            is EditNoteState.Editing -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        value = uiState.note.title,
                        onValueChange = { onEditNoteAction(EditNoteEvent.InputNoteTitle(it)) },
                        textStyle = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        placeholder = {
                            Text(
                                text = stringResource(Res.string.title),
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(
                                        alpha = 0.2f
                                    )
                                )
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        )
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 24.dp),
                        text = DateFormatter.formatDateToString(uiState.note.updatedAt),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    Content(
                        modifier = Modifier.weight(1f),
                        contents = uiState.note.content,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        onTextChange = { index, text ->
                            onEditNoteAction(EditNoteEvent.InputNoteContent(text, index))
                        },
                        onDeleteImageClick = {
                            onEditNoteAction(EditNoteEvent.RemoveImage(it))
                        }
                    )
                    Button(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.1f
                            ),
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        onClick = { onEditNoteAction(EditNoteEvent.Save) },
                        enabled = uiState.isSaveEnabled,
                    ) {
                        Text(
                            text = stringResource(Res.string.save_note),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            EditNoteState.Finished -> {
                LaunchedEffect(key1 = Unit) {
                    onBackClicked()
                }
            }

            EditNoteState.Loading -> {
                CircularProgressIndicator()
            }
        }

    }
}
