package com.commov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.BoringLayout
import android.widget.TextView
import android.widget.Toast
import com.commov.network.Endpoints

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val skipLogin = findViewById<TextView>(R.id.skip_login)
        skipLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun login(email: String, password: String): Boolean {
        val serviceURI = "${Endpoints.address}users/login";
        return true
    }
}
