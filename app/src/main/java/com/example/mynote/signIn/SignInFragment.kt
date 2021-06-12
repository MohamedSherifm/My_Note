package com.example.mynote.signIn

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.mynote.R
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentSigninBinding


class SignInFragment : Fragment() {
    lateinit var name:String
    lateinit var password:String
    var boxChecked:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as AppCompatActivity).supportActionBar?.title = "Sign In"
        val binding: FragmentSigninBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_signin, container, false)

        val application = requireNotNull(this.activity).application

        val notesSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = SignInViewModelFactory(notesSource, application)

        val signInViewModel = ViewModelProvider(this, viewModelFactory).get(SignInViewModel::class.java)

        binding.signInViewModel = signInViewModel

        binding.lifecycleOwner = this

        fun getInfo(){
            name = binding.userNameId.text.toString()
            password = binding.userPasswordId.text.toString()
            if(binding.keepMeSignedCheckBox.isChecked){
            boxChecked = 1
            }
        }

        binding.signInBtn.setOnClickListener{
            getInfo()
            signInViewModel.onSignInPressed(name, password, boxChecked)
        }

        signInViewModel.eventNavigation.observe(viewLifecycleOwner, Observer { user ->
            user?.let{
                this.findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToNotesScreenFragment(user.userName, user.userId))
                signInViewModel.onNavigationComplete()
            }
        })

        signInViewModel.showError.observe(viewLifecycleOwner, Observer {
            if(it == true){
                Toast.makeText(application,"Make sure the user name and password are correct",Toast.LENGTH_SHORT).show()
            }
        })

        binding.signUpText.setOnClickListener{
            this.findNavController().navigate(SignInFragmentDirections.actionSigninFragmentToSignUpFragment())
        }

        return binding.root

    }

}