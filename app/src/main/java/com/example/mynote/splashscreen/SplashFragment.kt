package com.example.mynote.splashscreen

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynote.R
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {
    private val handler = Handler()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSplashBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )

        val application = requireNotNull(this.activity).application

        val notesSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = SplashFragmentViewModelFactory(notesSource, application)

        val splashScreenViewModel =
            ViewModelProvider(this, viewModelFactory).get(SplashFragmentViewModel::class.java)

        binding.noteIconSplash.animation = AnimationUtils.loadAnimation(application, R.anim.top_animation)
        binding.textViewSplash.animation = AnimationUtils.loadAnimation(application, R.anim.bottom_animation)



        splashScreenViewModel.eventNavigate.observe(viewLifecycleOwner, Observer { user ->
            handler.postDelayed({
                if (user == null) {
                    println("Null")
                    this.findNavController()
                        .navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
                } else {
                    this.findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToNotesScreenFragment(
                            user.userName,
                            user.userId
                        )
                    )
                }

            }, 3000)
        })
        //splashScreenViewModel.eventNavigate

        return binding.root
    }


}