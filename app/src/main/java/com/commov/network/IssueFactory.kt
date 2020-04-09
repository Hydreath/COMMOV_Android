package com.commov.network

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

object IssueFactory {
    fun createIssue(
        context: Context,
        title: String,
        description: String,
        lat: Float,
        long: Float,
        photo: Any?,
        callback: (() -> Unit)
    ) {
        val serviceURI = "${Endpoints.address}issue";
        val body = JSONObject()
        body.put("title", title)
        body.put("description", description)
        body.put("lat", lat)
        body.put("long", long)
        // missing base64 image

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.POST,
            serviceURI,
            body,
            Response.Listener { response ->
                callback.invoke()
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    context,
                    "Some error occurred while creating a new issue!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                var params: MutableMap<String, String> = HashMap<String, String>()

                params["Authorization"] = "Bearer ${UserAuth.token}"
                return params
            }
        }

        RequestQueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }

    fun getAllIssues(context: Context, callback: ((JSONObject)-> Unit)){
        val serviceURI = "${Endpoints.address}issues";
        // missing base64 image

        val jsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            serviceURI,
            null,
            Response.Listener { response ->
                callback.invoke(response)
            },
            Response.ErrorListener { error ->
                Toast.makeText(
                    context,
                    "Some error occurred while getting all issues!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                var params: MutableMap<String, String> = HashMap<String, String>()

                params["Authorization"] = "Bearer ${UserAuth.token}"
                return params
            }
        }

        RequestQueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest)
    }
}