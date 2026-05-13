package com.bagmanov.kmpnotes.notes_manager.data.db

import androidx.room.TypeConverter
import com.bagmanov.kmpnotes.notes_manager.data.entity.ContentType

class Converters {
    @TypeConverter
    fun fromContentType(value: ContentType): String = value.name

    @TypeConverter
    fun toContentType(value: String): ContentType = ContentType.valueOf(value)
}
