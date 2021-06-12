package com.example.mynote.updateNote

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.signIn.SignInViewModel

class UpdateNoteViewModelFactory (
    private val dataSource: NoteDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateNoteViewModel::class.java)) {
            return UpdateNoteViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}