package com.example.mynote.notesScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.signIn.SignInViewModel

class NotesScreenViewModelFactory (
    private val dataSource: NoteDatabaseDao,
    private val application: Application,
    private val userId : Int
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesScreenViewModel::class.java)) {
            return NotesScreenViewModel(dataSource, application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}