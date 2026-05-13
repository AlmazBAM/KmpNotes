package com.bagmanov.kmpnotes.notes_manager.presentation.creation.state

import androidx.compose.runtime.Immutable
import com.bagmanov.kmpnotes.notes_manager.domain.model.ContentItem

@Immutable
data class CreateNoteState(
    val title: String = "",
    val content: List<ContentItem> = listOf(ContentItem.Text("")),

    ) {
    val isSaveEnabled: Boolean
        get() {
            return when {
                title.isBlank() -> false
                content.isEmpty() -> false
                else -> {
                    content.any {
                        it !is ContentItem.Text || it.text.isNotBlank()
                    }
                }
            }
        }
}