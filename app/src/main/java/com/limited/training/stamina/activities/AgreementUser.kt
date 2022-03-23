package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class AgreementUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_user)
    }

    override fun onStart() {
        super.onStart()

        // Busqueda de botones para transiciones
        val acceptButton : Button = findViewById(R.id.btn_accept_agreement_user)
        val cancelButton : Button = findViewById(R.id.btn_cancel_agreement_user)

        // Transicion hacia acuerdo de localización
        acceptButton.setOnClickListener {
            val intActivityAgreementLocation : Intent = Intent(applicationContext,
                AgreementLocation::class.java)
            startActivity(intActivityAgreementLocation)
        }

        // Transicion fuera de la aplicacion, sin acuerdo de usuario no se puede usar
        cancelButton.setOnClickListener {
            val intCancelButton : Intent = Intent(Intent.ACTION_MAIN)
            intCancelButton.addCategory(Intent.CATEGORY_HOME)
            startActivity(intCancelButton)
        }
    }
}