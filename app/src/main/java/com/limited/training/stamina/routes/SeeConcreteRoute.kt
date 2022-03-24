package com.limited.training.stamina.routes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.activities.ProgressActivity
import com.limited.training.stamina.activities.RegisterActivity
import com.limited.training.stamina.activities.StartActivity

var ratingButton: Button? = null
var startButton: Button? = null

class SeeConcreteRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_concrete)

        ratingButton = findViewById(R.id.rating_btn)
        ratingButton!!.setOnClickListener {
//            val intRatingRoute: Intent = Intent(applicationContext, ProgressActivity::class.java)
            Toast.makeText(this, "Se muestra la clasificación de la ruta", Toast.LENGTH_SHORT).show()
        }

        startButton = findViewById(R.id.start_btn)
        startButton!!.setOnClickListener {
            val intRouteInProgressActivity: Intent =
                Intent(applicationContext, RouteInProgress::class.java)
            startActivity(intRouteInProgressActivity)
        }
    }
}