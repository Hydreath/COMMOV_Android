package com.commov.ui.points

import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commov.R
import com.commov.data.issue.Issue

class PointItemContainer(itemView: View): RecyclerView.ViewHolder(itemView) {
    var pointId: Int = 0
    public val deleteButton: ImageButton = itemView.findViewById<ImageButton>(R.id.myPointDelete)

    public fun formatView(issue: Issue){
        itemView.findViewById<TextView>(R.id.myPointTitle).text = issue.title
        itemView.findViewById<TextView>(R.id.myPointDescription).text = issue.desc
        this.pointId = issue.id
    }
}