package com.commov

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.commov.Note.NoteAdapter
import com.commov.Note.data.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        val recyclerView = view.findViewById<RecyclerView>(R.id.note_list)
        val adapter = NoteAdapter(DatabaseHelper(this.context!!).getAllNotes())
        recyclerView.adapter = adapter


        // Making the navigator change the current fragment on fab click
        val fab: FloatingActionButton = view.findViewById(R.id.addButton)
        fab.setOnClickListener { view ->
            val navController = findNavController();
            navController.navigate(R.id.noteCreatorFragment)
        }

        (activity as AppCompatActivity).supportActionBar!!.title = "Notes"

        return view
    }
}
