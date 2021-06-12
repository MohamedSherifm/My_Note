package com.example.mynote.signIn

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.User
import kotlinx.coroutines.*

class SignInViewModel(
    val dataBase : NoteDatabaseDao,
    application: Application
):AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _eventNavigate = MutableLiveData<User>()
    val eventNavigation: LiveData<User>
    get() = _eventNavigate

    private val _showError = MutableLiveData<Boolean>()
    val showError: LiveData<Boolean>
    get() = _showError

    private val _signedInUser = MutableLiveData<Boolean>()
    val signedInUser: LiveData<Boolean>
    get() = _signedInUser

    init {

        onStartViewModel()

    }

    private suspend fun checkSignInUser():User {
        return withContext(Dispatchers.IO){
            dataBase.getSignedInUser()
        }
    }

    fun onStartViewModel(){
        uiScope.launch {
            val signedUser = checkSignInUser()
            if (signedUser != null ){
                onSignInPressed(signedUser.userName, signedUser.userPassword, signedUser.keepMeSigned)
                _eventNavigate.value = null
            }
        }
    }

    fun onSignInPressed(name:String, password:String, keepMeSignedIn:Int){
        uiScope.launch {
            val user = findUser(name, password)

            if(keepMeSignedIn ==1 && user!= null ){
                setAllKeepMeSignedInToFalse()
                updateKeepMeSignedIn(name)
            }

            if(user == null){
                _showError.value = true
            }
            else{

                _showError.value = false
                _eventNavigate.value = user
            }
        }
    }

    private suspend fun updateKeepMeSignedIn(userName: String) {
        withContext(Dispatchers.IO){
            dataBase.setKeepMeSignedInToTrue(userName)
        }
    }

    private suspend fun setAllKeepMeSignedInToFalse() {
        withContext(Dispatchers.IO){
            dataBase.setKeepMeSignedInToFalse()
        }
    }

    private suspend fun findUser(name: String, password: String): User? {
        return withContext(Dispatchers.IO){
            return@withContext dataBase.selectUSer(name, password)
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