package com.limited.training.stamina.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.limited.training.stamina.R
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
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_home_like);
        }

        val commentButton : ImageButton = binding.feedEntry1CommentBtn
        commentButton!!.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_home_comment);
        }

        val shareButton : ImageButton = binding.feedEntry1ShareBtn
        shareButton!!.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_home_to_navigation_home_share);
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}