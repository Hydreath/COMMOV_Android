package com.commov.network

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.Volley
import com.commov.data.user.User
import org.json.JSONObject

object UserAuth {
    var token: String = ""
    var isLoggedIn: Boolean = false

    fun login(context: Context, email: String, password: String, callback: (() -> Unit)): Boolean {
        val serviceURI = "${Endpoints.address}users/auth";
        val body = JSONObject()
        body.put("email", email)
        body.put("password", password)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, serviceURI, body,
            Response.Listener { response ->
                if(response.get("status").toString().equals("success")) {
                    this.token = response.get("token").toString()
                    println(this.token)
                    isLoggedIn = true
                    callback.invoke()
                }
            },
            Response.ErrorListener { error ->
                println(error.message)
                when (error.networkResponse.statusCode){
                    403 -> {
                        Toast.makeText(context, "Login information is not correct!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Couldn't reach the server!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        RequestQueueManager.getInstance(context).addToRequestQueue(jsonObjectRequest)

        return true
    }

    fun logout(){
        this.token = ""
        this.isLoggedIn = false
    }
}