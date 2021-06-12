package com.example.mynote.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "notes")
@Parcelize
data class Notes(

    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,

    @ColumnInfo(name = "note_title")
    var noteTitle: String,

    @ColumnInfo(name = "note_description")
    var noteDescription: String,

    var noteUserId: Int
): Parcelable
