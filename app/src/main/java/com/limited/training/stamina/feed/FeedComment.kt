package com.limited.training.stamina.feed
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.ui.home.HomeFragment

class FeedComment : AppCompatActivity() {
    private var publishButton : Button?= null
    private var commentEditText : EditText?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_comment)

//        commentEditText!!.requestFocus()
//        commentEditText = findViewById<EditText>(R.id.comment_pt)

        publishButton = findViewById(R.id.publish_btn)
        publishButton!!.setOnClickListener {
//            var commentText = commentEditText!!.text.toString()
            val commentText = "Comentario de prueba"
            Toast.makeText(this, "Comentario \"$commentText\" publicado", Toast.LENGTH_SHORT).show()
            val intHome = Intent(applicationContext, MainActivity::class.java)
            startActivity(intHome)
        }
    }
}