package com.limited.training.stamina.feed
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class FeedLike : AppCompatActivity() {
    private var commentButton : ImageButton?= null
    private var shareButton : ImageButton?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_like)

        commentButton = findViewById(R.id.PublicationEntry1Comment_btn)
        commentButton!!.setOnClickListener {
            val intComment = Intent(applicationContext, FeedComment::class.java)
            startActivity(intComment)
        }

        shareButton = findViewById(R.id.PublicationEntry1Share_btn)
        shareButton!!.setOnClickListener {
            val intShare = Intent(applicationContext, FeedShare::class.java)
            startActivity(intShare)
        }
    }
}