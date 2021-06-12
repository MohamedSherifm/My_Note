package com.example.mynote.updateNote

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mynote.R
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentUpdateNoteBinding
import com.google.android.material.snackbar.Snackbar


class UpdateNoteFragment : Fragment() {
    lateinit var noteTitle: String
    lateinit var noteDescription: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        (activity as AppCompatActivity).supportActionBar?.title = "Update Note"
        val binding: FragmentUpdateNoteBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_update_note,container, false
        )
        val note = UpdateNoteFragmentArgs.fromBundle(requireArguments()).noteDetail

        val application = requireNotNull(this.activity).application

        val dataSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = UpdateNoteViewModelFactory(dataSource, application)

        val updateNoteViewModel = ViewModelProvider(this, viewModelFactory).get(UpdateNoteViewModel::class.java)

        binding.updateNoteViewModel = updateNoteViewModel

        binding.lifecycleOwner = this

        val title = binding.updateNoteTitleId

        val description = binding.updateNoteDescriptionId

        title.setText(note.noteTitle)

        description.setText(note.noteDescription)

        fun updateInfo(){
            noteTitle = title.text.toString()
            noteDescription = description.text.toString()

            note.noteTitle = noteTitle
            note.noteDescription = noteDescription
        }

        binding.updateBtn.setOnClickListener{
            updateInfo()
            updateNoteViewModel.onUpdateBtnPressed(note)
            Snackbar.make(requireView(),"Note Updated", Snackbar.LENGTH_SHORT).show()
        }

        updateNoteViewModel.eventNavigate.observe(viewLifecycleOwner, Observer {
            if(it == true){
                this.findNavController().navigateUp()
                updateNoteViewModel.onNavigationComplete()
            }
        })



        return binding.root
    }


}