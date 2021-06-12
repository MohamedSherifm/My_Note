package com.example.mynote.signUp

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.User
import kotlinx.coroutines.*
import java.lang.Exception

class SignUpViewModel(
    val dataBase: NoteDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate = MutableLiveData<Boolean>()
    val eventNavigate: LiveData<Boolean>
        get() = _eventNavigate

    private val _sameUserException = MutableLiveData<Boolean>()
    val sameUserException : LiveData<Boolean>
    get() = _sameUserException



    fun onSignUpPressed(name: String, password: String, keepMeSigned: Int) {
        uiScope.launch {
            val newUser =
                User(userName = name, userPassword = password, keepMeSigned = keepMeSigned)
            if(insertUser(newUser)==1){
                _sameUserException.value = false
                _eventNavigate.value = true
            }
            else{
                _sameUserException.value = true
            }

        }


    }

    private suspend fun insertUser(newUser: User): Int {
         return withContext(Dispatchers.IO) {
            try{
            dataBase.insertUser(newUser)
                return@withContext 1
            }
            catch (e: SQLiteConstraintException){
                println("user exists")
                return@withContext 0

            }
        }
    }

    fun onNavigationComplete() {
        _eventNavigate.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}