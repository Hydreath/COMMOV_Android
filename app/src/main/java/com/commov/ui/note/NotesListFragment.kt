package com.commov.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.commov.MainActivity
import com.commov.ui.note.NoteAdapter
import com.commov.data.note.DatabaseHelper
import com.commov.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        val recyclerView = view.findViewById<RecyclerView>(R.id.note_list)
        val adapter =
            NoteAdapter(DatabaseHelper(this.context!!).getAllNotes())
        recyclerView.adapter = adapter


        // Making the navigator change the current fragment on fab click
        val fab: FloatingActionButton = view.findViewById(R.id.addButton)

        fab.setOnClickListener { view ->
            val navController = this.findNavController();
            navController.navigate(R.id.noteCreatorFragment)
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.noteListTitle)

        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                (activity as MainActivity).openDrawer()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
