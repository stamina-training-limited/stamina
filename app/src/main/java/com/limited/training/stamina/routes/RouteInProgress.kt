package com.limited.training.stamina.routes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.activities.ProgressActivity
import com.limited.training.stamina.activities.StartActivity

var stopButton : Button ?= null

class RouteInProgress : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_progress)

        stopButton = findViewById(R.id.rating_btn)
        stopButton!!.setOnClickListener {
            val intStartActivity: Intent = Intent(applicationContext, StartActivity::class.java)
            // cando estea lista cambiar por RatingRoute
            startActivity(intStartActivity)
        }
    }
}