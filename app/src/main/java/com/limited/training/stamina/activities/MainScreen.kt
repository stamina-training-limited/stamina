package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class MainScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }

    override fun onStart() {
        super.onStart()

        // Búsqueda de botones para transiciones
        val loginGoogleButton : Button = findViewById(R.id.btn_main_screen_login_google)

        // Transición a login de google
        loginGoogleButton.setOnClickListener {
            val intLoginGoogle : Intent = Intent(applicationContext, GoogleLogin::class.java)
            startActivity(intLoginGoogle)
        }
    }
}