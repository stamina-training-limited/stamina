package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.R

class GoogleLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_google)
    }

    override fun onStart() {
        super.onStart()

        // Busqueda de botones para transiciones
        val login : Button = findViewById(R.id.btn_login_google_login)

        // Transicion hacia pantalla de feed
        login.setOnClickListener {
            val intLogin : Intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intLogin)
        }
    }
}