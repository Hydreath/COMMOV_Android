package com.commov.ui.points

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commov.MainActivity

import com.commov.R
import com.commov.data.issue.Issue
import com.commov.network.IssueFactory
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class PointsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_points, container, false)

        IssueFactory.getAllUserIssues(this.context!!) {
            val items: ArrayList<Issue> = ArrayList()
            val array = it.get("data") as JSONArray
            for (i in 0 until array.length()) {
                val temp = array.getJSONObject(i)
                Issue(
                    temp.getInt("issueId"),
                    temp.getString("title"),
                    temp.getString("description"),
                    temp.getString("imagePath"),
                    temp.getDouble("lat"),
                    temp.getDouble("long"),
                    temp.getString("createdAt")
                ).also {
                    items.add(it)
                    println(it)
                }
            }


            val list: RecyclerView = view.findViewById<RecyclerView>(R.id.myPointsList)
            list.adapter = PointsListAdapter(items, this.context!!)
        }

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.added_points)

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
