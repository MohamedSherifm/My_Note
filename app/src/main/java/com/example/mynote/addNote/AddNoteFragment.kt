package com.example.mynote.addNote

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
import com.example.mynote.database.NoteDatabaseDao
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentAddNoteBinding
import com.google.android.material.snackbar.Snackbar


class AddNoteFragment : Fragment() {

    lateinit var noteTitle:String
    lateinit var noteDescription:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (activity as AppCompatActivity).supportActionBar?.title = "Add new Note"
        val arguments = AddNoteFragmentArgs.fromBundle(requireArguments())

        val binding: FragmentAddNoteBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_note, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = AddNoteViewModelFactory(dataSource, application)

        val addNoteViewModel = ViewModelProvider(this, viewModelFactory).get(AddNoteViewModel::class.java)

        binding.addNoteViewModel = addNoteViewModel

        binding.lifecycleOwner = this

        val noteTitleEditTextF = binding.noteTitleEditText
        val noteDescriptionEditTextF = binding.noteDescriptionEditText

        fun getInfo(){
            noteTitle = noteTitleEditTextF.text.toString()
            noteDescription = noteDescriptionEditTextF.text.toString()
        }
        binding.addBtn.setOnClickListener{
            getInfo()
            addNoteViewModel.onAddNotePressed(noteTitle, noteDescription, arguments.userId)
            Snackbar.make(requireView(),"Note added", Snackbar.LENGTH_LONG).show()
            noteTitleEditTextF.text = null
            noteDescriptionEditTextF.text = null
        }

        addNoteViewModel.eventNavigate.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigateUp()
                addNoteViewModel.onNavigationComplete()
            }
        })

        return binding.root
    }


}