package com.example.mynote.addNote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.Notes
import kotlinx.coroutines.*

class AddNoteViewModel(
    val dataBase: NoteDatabaseDao,
    application: Application
): AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate = MutableLiveData<Boolean>()
    val eventNavigate :LiveData<Boolean>
    get() = _eventNavigate


    fun onAddNotePressed(noteTitle: String, noteDescription:String, userId: Int){
        uiScope.launch {
            val newNote = Notes(noteTitle = noteTitle, noteDescription = noteDescription, noteUserId = userId)
            addNote(newNote)
            _eventNavigate.value = true
        }
    }

    private suspend fun addNote(newNote: Notes) {
        withContext(Dispatchers.IO){
            dataBase.insertNote(newNote)
        }
    }

    fun onNavigationComplete(){
        _eventNavigate.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}