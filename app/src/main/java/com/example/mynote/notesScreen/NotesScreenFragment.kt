package com.example.mynote.notesScreen

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.R
import com.example.mynote.database.NotesDatabase
import com.example.mynote.databinding.FragmentNotesScreenBinding
import com.example.mynote.signIn.SignInViewModelFactory
import com.google.android.material.snackbar.Snackbar


class NotesScreenFragment : Fragment() {

    private var signOutTriggered = MutableLiveData<Boolean>()
    private var deleteAllBtnTriggered = MutableLiveData<Boolean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val arguments = NotesScreenFragmentArgs.fromBundle(requireArguments())

        val binding: FragmentNotesScreenBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_notes_screen, container, false)

        val application = requireNotNull(this.activity).application

        val notesSource = NotesDatabase.getInstance(application).noteDatabaseDao

        val viewModelFactory = NotesScreenViewModelFactory(notesSource, application, arguments.userId)

        val notesScreenViewModel = ViewModelProvider(this, viewModelFactory).get(NotesScreenViewModel::class.java)

        (activity as AppCompatActivity).supportActionBar?.title = "${arguments.userName.toString().capitalize()}"

        binding.notesScreenViewModel = notesScreenViewModel

        binding.lifecycleOwner = this

        val adapter = NotesAdapter(NotesClickListener {
            note -> notesScreenViewModel.onNoteClicked(note)
        })

        val recyclerViewList = binding.notesList

        recyclerViewList.adapter = adapter

        notesScreenViewModel.navigateToUpdateNote.observe(viewLifecycleOwner, Observer { note ->
            note?.let{
                this.findNavController().navigate(NotesScreenFragmentDirections.actionNotesScreenFragmentToUpdateNoteFragment(note))
                notesScreenViewModel.onNavigateToUpdateNoteCompleted()
            }
        })

        notesScreenViewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
        notes?.let {
            adapter.submitList(notes)
        }
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.currentList[viewHolder.adapterPosition]
                notesScreenViewModel.onSwiped(note)
                Snackbar.make(requireView(),"Note deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO"){
                        notesScreenViewModel.onUndoDeletePressed(note)
                    }.show()
            }
        }).attachToRecyclerView(recyclerViewList)

        setHasOptionsMenu(true)

        signOutTriggered.observe(viewLifecycleOwner, Observer {
            if(it==true){
            notesScreenViewModel.onSignOutPressed()
            println("hello")

            }
        })

        deleteAllBtnTriggered.observe(viewLifecycleOwner, Observer {
            if(it == true){
                notesScreenViewModel.onDeleteAllPressed()
                deleteAllBtnTriggered.value = false

                /*var builder:AlertDialog.Builder = AlertDialog.Builder(application.applicationContext)
                builder.setTitle("Confirm Delete")
                builder.setMessage("Are you sure you want to delete all notes, it can't be recovered")
                builder.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialog, id ->

                    dialog.dismiss()
                })

                builder.setNegativeButton("No", DialogInterface.OnClickListener{dialog, id ->
                    dialog.dismiss()
                })

                var alert:AlertDialog = builder.create()
                alert.show()*/
            }
        })

        notesScreenViewModel.eventNavigate.observe(viewLifecycleOwner, Observer {
            if(it==true){
                this.findNavController().navigate(NotesScreenFragmentDirections.actionNotesScreenFragmentToSignInFragment())
                notesScreenViewModel.onNavigationComplete()

            }
        })

        binding.addNoteBtn.setOnClickListener{
            this.findNavController().navigate(NotesScreenFragmentDirections.actionNotesScreenFragmentToAddNoteFragment(arguments.userId))
        }



        return binding.root


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if(item.itemId==R.id.signInFragment){
                signOutTriggered.value = true
            }
            if(item.itemId == R.id.delete_all_btn){
                deleteAllBtnTriggered.value = true
            }

            return super.onOptionsItemSelected(item)
        }


}