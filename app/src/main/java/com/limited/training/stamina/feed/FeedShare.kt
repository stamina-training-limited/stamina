package com.limited.training.stamina.feed

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.limited.training.stamina.R

class FeedShare  : AppCompatActivity() {
    private var likeButton : ImageButton?= null
    private var commentButton : ImageButton?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_share)

        likeButton = findViewById(R.id.feedEntry1Like_btn)
        likeButton!!.setOnClickListener {
            val intLike = Intent(applicationContext, FeedLike::class.java)
            startActivity(intLike)
        }

        commentButton = findViewById(R.id.feedEntry1Comment_btn)
        commentButton!!.setOnClickListener {
            val intComment = Intent(applicationContext, FeedComment::class.java)
            startActivity(intComment)
        }
    }
}