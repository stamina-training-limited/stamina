package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

var stopButton : Button ?= null

class ProgressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_progress)

        stopButton = findViewById(R.id.stop_btn)
        stopButton!!.setOnClickListener {
            val intActivityPaused : Intent = Intent(applicationContext, PausedActivity::class.java)
            startActivity(intActivityPaused)
        }
    }
}