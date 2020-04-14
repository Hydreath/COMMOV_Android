package com.commov.ui.note


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.commov.data.note.DatabaseHelper
import com.commov.data.note.Note
import com.commov.R
import java.util.*

class NoteCreatorFragment : Fragment() {
    private val note: Note =
        Note()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_creator, container, false)

        // add back button
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.createNoteTitle)
        setHasOptionsMenu(true)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.card_save_options, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendar()
    }

    private fun validateData(view: View): Boolean{
        val title = view.findViewById<EditText>(R.id.editTitle).text.toString()
        val desc: String = view.findViewById<EditText>(R.id.editDesc).text.toString()

        if(desc.isEmpty() || title.isEmpty()) {
            Toast.makeText(view.context, getString(R.string.emptyDescription), Toast.LENGTH_SHORT).show()
            return false
        }

        this.note.description = desc
        this.note.title = title

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                findNavController().popBackStack()
                findNavController().navigate(R.id.notesList)
            }
            R.id.saveItem -> {
                if(!validateData(this.view!!))
                    return false
                else {
                    val db =
                        DatabaseHelper(this.view!!.context)
                    db.addNote(this.note)
                    val navController = findNavController();
                    findNavController().popBackStack()
                    navController.navigate(R.id.notesList)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //(activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun setupCalendar() {
        this.view?.findViewById<CalendarView>(R.id.dayContainer)?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            this.note.relevantAt = GregorianCalendar(year, month, dayOfMonth).time
        }
    }
}
