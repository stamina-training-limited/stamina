package com.limited.training.stamina.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

var resumeButton : Button?= null
var finishButton : Button ?= null

class PausedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_paused)

        resumeButton = findViewById(R.id.resume_btn)
        resumeButton!!.setOnClickListener {
            val intActivityProgress: Intent = Intent(applicationContext, ProgressActivity::class.java)
            startActivity(intActivityProgress)
        }

        finishButton = findViewById(R.id.finish_btn)
        finishButton!!.setOnClickListener {
            val intActivityRegister: Intent =
                Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intActivityRegister)
        }
    }
}