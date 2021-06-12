package com.example.mynote.notesScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.Notes
import kotlinx.coroutines.*

class NotesScreenViewModel(
    val dataBase: NoteDatabaseDao,
    application: Application,
    val userIdV: Int
):AndroidViewModel(application) {

    private var viewModelJob= Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate= MutableLiveData<Boolean>()
    val eventNavigate:LiveData<Boolean>
    get() = _eventNavigate

    private val _notes = dataBase.getUserNotes(userIdV)
    val notes:LiveData<List<Notes>>
    get() = _notes

    private val _navigateToUpdateNote = MutableLiveData<Notes>()
    val navigateToUpdateNote: LiveData<Notes>
    get() = _navigateToUpdateNote


    fun onNoteClicked(note: Notes){
        _navigateToUpdateNote.value = note
    }

    fun onNavigateToUpdateNoteCompleted(){
        _navigateToUpdateNote.value = null
    }


    fun onSignOutPressed(){
        uiScope.launch {
            signOutAllUsers()
            _eventNavigate.value = true
        }
    }

    private suspend fun signOutAllUsers() {
        withContext(Dispatchers.IO){
            dataBase.setKeepMeSignedInToFalse()

        }
    }

    fun onNavigationComplete(){
        _eventNavigate.value = false
    }

    fun onDeleteAllPressed(){
        uiScope.launch {
            deleteUserNotes(userIdV)
        }
    }

    fun onSwiped(note: Notes){
        uiScope.launch {
            deleteThisNote(note)
        }
    }



    private suspend fun deleteThisNote(note: Notes) {
        withContext(Dispatchers.IO){
            dataBase.deleteThisNote(note)
        }
    }

    private suspend fun deleteUserNotes(userIdV: Int) {
        withContext(Dispatchers.IO){
            dataBase.deleteUserNotes(userIdV)
        }
    }

    fun onUndoDeletePressed(note : Notes){
        uiScope.launch {
            undoDelete(note)
        }
    }

    private suspend fun undoDelete(note: Notes) {
        withContext(Dispatchers.IO){
            dataBase.insertNote(note)
        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}