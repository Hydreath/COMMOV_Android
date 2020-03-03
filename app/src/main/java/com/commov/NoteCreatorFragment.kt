package com.commov


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.commov.Note.data.DatabaseHelper
import com.commov.Note.data.Note
import kotlinx.android.synthetic.main.fragment_note_creator.*
import java.util.*

class NoteCreatorFragment : Fragment() {

    private var dayWeekContainer: View? = null
    private var dayContainer: View? = null
    private var timePeriodContainer: View? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_note_creator, container, false)

        this.dayContainer = view.findViewById(R.id.dayContainer)
        this.timePeriodContainer = view.findViewById(R.id.timePeriodContainer)
        this.dayWeekContainer = view.findViewById(R.id.dayWeekContainer)

        // Add days to spinner with an adapter
        val spinner: Spinner = view.findViewById(R.id.dayWeekContainer)
        ArrayAdapter.createFromResource(view.context, R.array.weekdays, android.R.layout.simple_spinner_dropdown_item).also {adapter ->
            spinner.adapter = adapter
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Interchange control type on radio change
        val radioOptions: RadioGroup? = view.findViewById(R.id.radioGroup)
        radioOptions?.setOnCheckedChangeListener { group, checked ->
            this.hideAll()
            when (checked) {
                R.id.weekDay -> {
                    this.dayWeekContainer?.visibility = View.VISIBLE
                    Toast.makeText(view.context, "WEEK DAY", Toast.LENGTH_SHORT).show()
                }
                R.id.timePeriod -> {
                    this.timePeriodContainer?.visibility = View.VISIBLE
                    Toast.makeText(view.context, "TIME PERIOD", Toast.LENGTH_SHORT).show()

                }
                R.id.day -> {
                    view.findViewById<CalendarView>(R.id.dayContainer).visibility = View.VISIBLE
                    Toast.makeText(view.context, "DAY", Toast.LENGTH_SHORT).show()
                }
            }
        }
        radioOptions?.check(R.id.day)

        view.findViewById<Button>(R.id.save).setOnClickListener{buttonView ->
            val note = this.validateData(view)
            if(note != null){
                val db = DatabaseHelper(view.context)
                db.addNote(note)
            }

        }
    }

    private fun hideAll(){
        this.dayWeekContainer?.visibility = View.GONE
        this.timePeriodContainer?.visibility = View.GONE
        this.dayContainer?.visibility = View.GONE

    }

    private fun validateData(view: View): Note?{
        var note = Note()
        val desc = view.findViewById<EditText>(R.id.editName).text.toString()
        note.description = desc
        if(desc.isEmpty()) {
            Toast.makeText(view.context, "The description text is empty", Toast.LENGTH_SHORT).show()
            return null
        }

        view.findViewById<RadioGroup>(R.id.radioGroup).also { radioGroup ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.weekDay -> {
                    note.weekDays = 0
                }
                R.id.timePeriod -> {
                    note.startDate = Date(view.findViewById<CalendarView>(R.id.startDate).date)
                    note.endDate = Date(view.findViewById<CalendarView>(R.id.endDate).date)
                }
                R.id.day -> {
                    note.startDate = Date(view.findViewById<CalendarView>(R.id.dayContainer).date)
                }
                else -> {
                    return null
                }
            }
        }
        return note
    }
}
