package com.example.mynote.signUp

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
import com.example.mynote.R
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    lateinit var name:String
    lateinit var password:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as AppCompatActivity).supportActionBar?.title = "Sign Up"
        val binding: FragmentSignUpBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign_up, container,false
        )

        val application = requireNotNull(this.activity).application

        val notesSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = SignUpViewModelFactory(notesSource, application)

        val signUpViewModel = ViewModelProvider(this , viewModelFactory).get(SignUpViewModel::class.java)

        binding.signUpViewModel = signUpViewModel

        binding.lifecycleOwner = this


        fun getInfo(){
            name = binding.signUpName.text.toString()
            password = binding.signUpPassword.text.toString()

        }

        fun showSameUserException(){
            binding.sameUserNameException.visibility = View.VISIBLE
        }


        binding.signUpBtn.setOnClickListener{
            getInfo()
            signUpViewModel.onSignUpPressed(name= name , password= password, 0)
        }

        signUpViewModel.eventNavigate.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
                    //Toast.makeText(application,"User ${name} was added",Toast.LENGTH_SHORT).show()
                signUpViewModel.onNavigationComplete()
            }

        })

        signUpViewModel.sameUserException.observe(viewLifecycleOwner, Observer {
            if(it == true){
                showSameUserException()
            }
        })

        return binding.root

    }


}