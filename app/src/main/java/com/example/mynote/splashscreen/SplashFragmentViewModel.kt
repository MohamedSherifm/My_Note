package com.example.mynote.splashscreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.User
import kotlinx.coroutines.*

class SplashFragmentViewModel(
    val dataBase: NoteDatabaseDao,
    application:Application
): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate = MutableLiveData<User>()
    val eventNavigate: LiveData<User>
    get() = _eventNavigate

    init {
        onStartViewModel()
    }

    private fun onStartViewModel() {
        uiScope.launch {
            val signedUser = checkSignInUser()
            if(signedUser!=null){
                _eventNavigate.value = signedUser
            }
            else {
                _eventNavigate.value = null
            }
        }
    }

    private suspend fun checkSignInUser(): User {
        return withContext(Dispatchers.IO){
            dataBase.getSignedInUser()
        }
    }

    fun onNavigationComplete(){
        _eventNavigate.value = null
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}