package com.example.mynote.updateNote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.Notes
import kotlinx.coroutines.*

class UpdateNoteViewModel(
    val dataBase : NoteDatabaseDao,
    application: Application
): AndroidViewModel(application) {

    private var viewModelJob= Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate = MutableLiveData<Boolean>()
    val eventNavigate: LiveData<Boolean>
    get() = _eventNavigate

    fun onUpdateBtnPressed(note: Notes){
        uiScope.launch {
            updateNote(note)
            _eventNavigate.value = true
        }

    }

    private suspend fun updateNote(note: Notes) {
        withContext(Dispatchers.IO){
            dataBase.updateNote(note)
        }

    }

    fun onNavigationComplete(){
        _eventNavigate.value = null
    }

}