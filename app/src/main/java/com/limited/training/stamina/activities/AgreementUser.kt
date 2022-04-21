package com.limited.training.stamina.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R


class AgreementUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_user)

        // Se comprueba si el usuario ya ha aceptado las condiciones de uso de la aplicacion
        val mPrefs = getSharedPreferences("acuerdoUsuario", 0)
        val acuerdoAceptado = mPrefs.getString("aceptado", "no")

        // Si ya ha aceptado, se pasa a login
        if(acuerdoAceptado.equals("si")){
            val intActivityAgreementLocation : Intent = Intent(applicationContext,
                MainScreen::class.java)
            startActivity(intActivityAgreementLocation)
        }
    }

    override fun onStart() {
        super.onStart()

        // Busqueda de botones para transiciones
        val acceptButton : Button = findViewById(R.id.btn_accept_agreement_user)
        val cancelButton : Button = findViewById(R.id.btn_cancel_agreement_user)

        // Transicion hacia login
        acceptButton.setOnClickListener {

            // Se guarda la preferencia del usuario si acepta

            val mPrefs = getSharedPreferences("acuerdoUsuario", 0)
            val mEditor: SharedPreferences.Editor = mPrefs.edit()
            mEditor.putString("aceptado", "si").commit()

            val intActivityAgreementLocation : Intent = Intent(applicationContext,
                MainScreen::class.java)
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