package com.commov.ui.points

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commov.R
import com.commov.data.issue.Issue
import com.commov.network.IssueFactory


class PointsListAdapter(private var issues: ArrayList<Issue>, private var context: Context): RecyclerView.Adapter<PointItemContainer>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointItemContainer {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.my_point_container, parent, false) as View
        return PointItemContainer(view)
    }

    override fun getItemCount(): Int {
        return issues.size
    }

    override fun onBindViewHolder(holder: PointItemContainer, position: Int) {
        holder.formatView(issues[position])
        holder.deleteButton.setOnClickListener {
            IssueFactory.deleteIssue(context, holder.pointId){
                this.issues.removeAt(position);
                this.notifyDataSetChanged();
            }
        }
    }
}