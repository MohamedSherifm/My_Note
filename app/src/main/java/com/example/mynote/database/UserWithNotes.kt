package com.example.mynote.database

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mynote.database.Notes
import com.example.mynote.database.User

data class UserWithNotes(
    @Embedded val user:User ,

    @Relation(
        parentColumn = "userId",
        entityColumn = "noteUserId"
    )
    val notes: List<Notes>
)