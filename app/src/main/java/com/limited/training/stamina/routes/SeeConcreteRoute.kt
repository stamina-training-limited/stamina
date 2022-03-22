package com.limited.training.stamina.routes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.activities.ProgressActivity
import com.limited.training.stamina.activities.RegisterActivity

var ratingButton : Button?= null
var startButton : Button ?= null

class ConcreteRoute : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_concrete)

        ratingButton = findViewById(R.id.rating_btn)
        ratingButton!!.setOnClickListener {
            val intRatingRoute: Intent = Intent(applicationContext, ProgressActivity::class.java)
            // cando estea lista cambiar por RatingRoute
            startActivity(intRatingRoute)
        }

        startButton = findViewById(R.id.start_btn)
        startButton!!.setOnClickListener {
            val intRouteInProgressActivity: Intent =
                Intent(applicationContext, RegisterActivity::class.java)
            // cando estea lista cambiar por ProgressRoute
            startActivity(intRouteInProgressActivity)
        }
    }
}