package com.commov


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.commov.Note.data.DatabaseHelper
import com.commov.Note.data.Note
import java.util.*

class NoteCreatorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_creator, container, false)

        // add back button
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_camera)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.save).setOnClickListener{buttonView ->
            val note = this.validateData(view)
            if(note != null){
                val db = DatabaseHelper(view.context)
                db.addNote(note)
            }
        }

        (activity as AppCompatActivity).supportActionBar!!.title = "Create Note"
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun validateData(view: View): Note?{
        val title = view.findViewById<EditText>(R.id.editTitle).text.toString()
        val desc: String = view.findViewById<EditText>(R.id.editDesc).text.toString()
        val date: Date = Date(view.findViewById<CalendarView>(R.id.dayContainer).date)

        if(desc.isEmpty() || title.isEmpty()) {
            Toast.makeText(view.context, "The description text is empty", Toast.LENGTH_SHORT).show()
            return null
        }
        return Note(title, desc, date)
    }
}
