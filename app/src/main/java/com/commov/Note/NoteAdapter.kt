package com.commov.Note

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.commov.Note.data.DatabaseHelper
import com.commov.Note.data.Note
import com.commov.R

class NoteAdapter(private var data : ArrayList<Note>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.note, parent, false) as View

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.updateContent(this.data[position])
        holder.onDeleteClickListen {
            if(DatabaseHelper(it.context).deleteNote(this.data[position])) {
                this.data.removeAt(position);
                this.notifyDataSetChanged();
            }
        }
        holder.onEditClickListe {
            Toast.makeText(it.context, "Not implemented yet", Toast.LENGTH_SHORT).show()
        }
    }
}