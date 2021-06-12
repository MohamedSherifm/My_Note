package com.example.mynote.notesScreen

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.mynote.database.Notes

@BindingAdapter("noteTitle")
fun TextView.setNotesTitle(item: Notes?){
    item?.let{
        text = item.noteTitle
    }
}

@BindingAdapter("noteDescription")
fun TextView.setNotesDescription(item: Notes?){
    item?.let{
        text = item.noteDescription
    }
}