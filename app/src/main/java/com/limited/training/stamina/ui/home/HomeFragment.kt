package com.limited.training.stamina.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.feed.FeedComment
import com.limited.training.stamina.feed.FeedLike
import com.limited.training.stamina.feed.FeedShare

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val likeButton: ImageButton = binding.feedEntry1LikeBtn
        likeButton!!.setOnClickListener {
            val intLike = Intent(activity, FeedLike::class.java)
            startActivity(intLike)
        }

        val commentButton : ImageButton = binding.feedEntry1CommentBtn
        commentButton!!.setOnClickListener {
            val intComment = Intent(activity, FeedComment::class.java)
            startActivity(intComment)
        }

        val shareButton : ImageButton = binding.feedEntry1ShareBtn
        shareButton!!.setOnClickListener {
            val intLike = Intent(activity, FeedShare::class.java)
            startActivity(intLike)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}