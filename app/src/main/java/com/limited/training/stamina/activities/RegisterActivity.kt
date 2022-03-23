package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

var discardButton : Button ?= null
var saveButton : Button ?= null
var routeNameEditText : EditText ?= null

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_register)

        routeNameEditText = findViewById(R.id.routeName_et) as EditText
        routeNameEditText!!.requestFocus()

        discardButton = findViewById(R.id.discard_btn)
        discardButton!!.setOnClickListener {
            // Provisional para pruebas
            // Redirigirá a pantalla principal
            Toast.makeText(this, "Actividad descartada", Toast.LENGTH_SHORT).show()
            val intStartActivity : Intent = Intent(applicationContext, StartActivity::class.java)
            startActivity(intStartActivity)
        }

        saveButton = findViewById(R.id.save_btn)
        saveButton!!.setOnClickListener {

            var routeName = routeNameEditText!!.text.toString()
            // Provisional para pruebas
            // Redirigirá a pantalla principal
            Toast.makeText(this, "Actividad " + routeName + " guardada", Toast.LENGTH_SHORT).show()
            val intStartActivity : Intent = Intent(applicationContext, StartActivity::class.java)
            startActivity(intStartActivity)
        }

    }
}