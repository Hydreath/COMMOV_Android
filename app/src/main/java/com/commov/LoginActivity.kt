package com.commov

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.commov.network.Endpoints
import com.commov.network.UserAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val skipLogin = findViewById<TextView>(R.id.skip_login)
        val login: Button = findViewById<Button>(R.id.login)

        login.setOnClickListener {
            val password: String = findViewById<TextView>(R.id.password).text.toString()
            val email: String = findViewById<TextView>(R.id.email).text.toString()
            // login and callback because of async like code?
            UserAuth.login(applicationContext, email, password) {
                changeToMain()
            }
        }

        skipLogin.setOnClickListener {
            changeToMain()
        }
    }

    fun changeToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}
