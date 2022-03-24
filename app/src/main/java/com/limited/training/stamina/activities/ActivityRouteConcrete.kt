package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class ActivityRouteConcrete: AppCompatActivity() {

    var startButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_start)

        /*
        startButton = findViewById(R.id.start_btn)
        startButton!!.setOnClickListener {
            val intActivityInProgress: Intent =
                Intent(applicationContext, ProgressActivity::class.java)
            startActivity(intActivityInProgress)
        }
        */
    }
}