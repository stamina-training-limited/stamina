package com.limited.training.stamina

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routes_watch)
        var toolbar: Toolbar = findViewById(R.id.tool_bar);

        if (toolbar != null) {
            toolbar.title = "Rutas"
            toolbar.setTitleTextColor(Color.WHITE)
            toolbar.setSubtitleTextColor(Color.WHITE)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true);
            toolbar.bringToFront()
        }

    }

}