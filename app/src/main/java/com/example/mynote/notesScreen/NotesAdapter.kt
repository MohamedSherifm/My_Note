package com.example.mynote.notesScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynote.database.Notes
import com.example.mynote.databinding.ListItemNoteBinding
import java.util.concurrent.TimeUnit

class NotesAdapter(val clickListener: NotesClickListener) : ListAdapter<Notes, NotesAdapter.ViewHolder>(NotesDiffCallBack()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: ListItemNoteBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notes, clickListener: NotesClickListener){
            binding.notes = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }
}

class NotesClickListener(val clickListener: (note: Notes)-> Unit) {
    fun onClick(note: Notes) =clickListener(note)

}

class NotesDiffCallBack: DiffUtil.ItemCallback<Notes>() {
    override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
        return oldItem == newItem
    }
}
