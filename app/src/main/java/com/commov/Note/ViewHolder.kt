package com.commov.Note

import android.view.ContextMenu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commov.R
import com.commov.Note.data.Note
import java.text.SimpleDateFormat
import java.util.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    private val titleHolder: TextView = itemView.findViewById<TextView>(R.id.title)
    private val descriptionHolder: TextView = itemView.findViewById<TextView>(R.id.description)
    private val menuOptions: ImageButton = itemView.findViewById(R.id.optionsCard)
    private val dateHolder: TextView = itemView.findViewById(R.id.noteDate)
    // this could be an anti pattern, dont know though
    private lateinit var note: Note

    lateinit var mOnClickDelete: ((View) -> Unit)
    lateinit var mOnClickEdit: ((View) -> Unit)

    init {
        this.menuOptions.setOnClickListener{ view ->
            val popup: PopupMenu = PopupMenu(view.context, view)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.card_options, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId){
                    R.id.editCard -> {
                        mOnClickEdit.invoke(view)
                    }
                    R.id.removeCard -> {
                        mOnClickDelete.invoke(view)
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popup.show()
        }
    }

    fun updateContent(note: Note) {
        this.note = note
        titleHolder.text = note.title
        descriptionHolder.text = note.description
        val dateFormatter: SimpleDateFormat = SimpleDateFormat("dd-MM-YYYY", Locale.GERMAN)
        dateHolder.text = dateFormatter.format(note.relevantAt)
    }

    fun onDeleteClickListen(f: (View) -> Unit){
        this.mOnClickDelete = f
    }

    fun onEditClickListen(f: (View) -> Unit){
        this.mOnClickEdit = f
    }
}