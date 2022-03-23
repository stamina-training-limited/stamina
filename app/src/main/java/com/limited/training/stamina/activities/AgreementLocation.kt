package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class AgreementLocation : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_location)
    }

    override fun onStart() {
        super.onStart()

        // Busqueda de botones para transiciones
        val acceptButton : Button = findViewById(R.id.btn_accept_agreement_location)
        val cancelButton : Button = findViewById(R.id.btn_cancel_agreement_location)

        // Transicion hacia pantalla principal. La localización está activada
        acceptButton.setOnClickListener {
            val intMainScreen : Intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intMainScreen)
        }

        // Transicion hacia pantalla principal. La localización está desactivada
        cancelButton.setOnClickListener {
            val intMainScreen : Intent = Intent(applicationContext, MainScreen::class.java)
            startActivity(intMainScreen)
        }
    }
}