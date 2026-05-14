package com.bagmanov.kmpnotes.notes_manager.presentation.creation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagmanov.kmpnotes.core.presentation.Content
import com.bagmanov.kmpnotes.core.presentation.utils.DateFormatter
import com.bagmanov.kmpnotes.notes_manager.presentation.creation.state.CreateNoteState
import com.bagmanov.kmpnotes.core.presentation.ui.icons.CustomIcons
import com.bagmanov.kmpnotes.core.presentation.utils.rememberImagePickerLauncher
import kmpnotes.composeapp.generated.resources.Res
import kmpnotes.composeapp.generated.resources.create_note
import kmpnotes.composeapp.generated.resources.save_note
import kmpnotes.composeapp.generated.resources.title
import org.jetbrains.compose.resources.stringResource

const val TAG = "CreateNoteScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNoteScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onCreateNoteAction: (CreateNoteEvent) -> Unit,
    uiState: CreateNoteState,
) {

    val imagePickerLauncher = rememberImagePickerLauncher { path ->
        path?.let {
            onCreateNoteAction(CreateNoteEvent.ImagePicked(it))
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.create_note),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 20.sp
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .clickable {
                                onBackClicked()
                            },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close create note screen"
                    )
                },
                actions = {
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .clickable {
                                imagePickerLauncher.launch()
                            },
                        imageVector = CustomIcons.AddPhoto,
                        contentDescription = "Add photo from gallery",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                value = uiState.title,
                onValueChange = { onCreateNoteAction(CreateNoteEvent.InputNoteTitle(it)) },
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
                text = DateFormatter.formatCurrentDate(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Content(
                modifier = Modifier.weight(1f),
                contents = uiState.content,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                onTextChange = { index, text ->
                    onCreateNoteAction(
                        CreateNoteEvent.InputNoteContent(
                            content = text,
                            index = index
                        )
                    )
                },
                onDeleteImageClick = {
                    onCreateNoteAction(CreateNoteEvent.RemoveImage(it))
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
                onClick = {
                    onCreateNoteAction(CreateNoteEvent.Save)
                    onBackClicked()
                },
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
}