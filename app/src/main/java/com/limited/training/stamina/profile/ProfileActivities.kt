package com.limited.training.stamina.profile

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.R


class ProfileActivities : AppCompatActivity() {
    private var profileTextView : TextView?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_activities)

        profileTextView = findViewById(R.id.activitiesProfileSection_tv)
        profileTextView!!.setOnClickListener {
            val intProfileMain = Intent(this@ProfileActivities,MainActivity::class.java)
            intProfileMain.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            finish()

//            val intComment = Intent(applicationContext, MainActivity::class.java)
//            startActivity(intComment)
        }
    }
}