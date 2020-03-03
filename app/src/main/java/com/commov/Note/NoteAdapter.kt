package com.commov.Note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commov.Note.data.DatabaseHelper
import com.commov.Note.data.Note
import com.commov.R

class NoteAdapter(private var data : ArrayList<Note>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.data = DatabaseHelper(parent.context).getAllNotes()
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note, parent, false) as View

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleHolder.text = this.data.get(position).description
    }
}