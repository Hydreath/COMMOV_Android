package com.commov

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.commov.Note.data.DatabaseHelper
import com.commov.Note.data.Note
import java.util.*

class NoteEditorFragment : Fragment() {
    lateinit var note: Note

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_note_creator, container, false)
        // cancel button
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = "Edit Note"
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //super.onViewCreated(view, savedInstanceState)

        this.note = arguments?.get("note")!! as Note
        this.populateFields(note)
        view.findViewById<CalendarView>(R.id.dayContainer).setOnDateChangeListener { view, year, month, dayOfMonth ->
            this.note.relevantAt = GregorianCalendar(year, month, dayOfMonth).time
            println("Changed date test")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_save_options, menu)
    }

    private fun populateFields(note: Note) {
        this.view!!.findViewById<EditText>(R.id.editTitle).setText(note.title)
        this.view!!.findViewById<EditText>(R.id.editDesc).setText(note.description)
        this.view!!.findViewById<CalendarView>(R.id.dayContainer).date = note.relevantAt.time
    }

    private fun validateData(view: View): Boolean {
        val title = view.findViewById<EditText>(R.id.editTitle).text.toString()
        val desc: String = view.findViewById<EditText>(R.id.editDesc).text.toString()

        if (desc.isEmpty() || title.isEmpty()) {
            Toast.makeText(view.context, "The description text is empty", Toast.LENGTH_SHORT).show()
            return false
        }
        this.note.title = title
        this.note.description = desc
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                findNavController().navigate(R.id.notesListFragment)
            }
            R.id.saveItem -> {
                if(!validateData(this.view!!))
                    return false
                else {
                    // Call database function and redirect
                    val db: DatabaseHelper = DatabaseHelper(context!!)
                    if(db.updateNote(this.note)) {
                        findNavController().popBackStack()
                        findNavController().navigate(R.id.notesListFragment)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }
}