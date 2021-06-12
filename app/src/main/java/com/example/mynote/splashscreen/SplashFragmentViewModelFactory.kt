package com.example.mynote.splashscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.signIn.SignInViewModel

class SplashFragmentViewModelFactory (
    private val dataSource: NoteDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashFragmentViewModel::class.java)) {
            return SplashFragmentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}