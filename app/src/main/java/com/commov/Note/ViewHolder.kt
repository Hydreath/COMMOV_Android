package com.commov.Note

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commov.R
import org.w3c.dom.Text

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val titleHolder: TextView = itemView.findViewById<TextView>(R.id.title)
    val descriptionHolder: TextView = itemView.findViewById<TextView>(R.id.description)

}