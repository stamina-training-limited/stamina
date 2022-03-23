package com.limited.training.stamina.routes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

var stopButton: Button? = null

class RouteInProgress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_progress)

        stopButton = findViewById(R.id.stop_btn)
        stopButton!!.setOnClickListener {
            val intFinishRouteActivity: Intent =
                Intent(applicationContext, SeeRoutes::class.java)
            startActivity(intFinishRouteActivity)
        }
    }
}