package com.limited.training.stamina.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.databinding.FragmentProfileBinding
import com.limited.training.stamina.feed.FeedComment
import com.limited.training.stamina.feed.FeedLike
import com.limited.training.stamina.feed.FeedShare
import com.limited.training.stamina.profile.ProfileActivities

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val activitiesTextView : TextView = binding.profileActivitiesSectionTv
        activitiesTextView!!.setOnClickListener {
            val intLike = Intent(activity, ProfileActivities::class.java)
            startActivity(intLike)
        }

        val editProfileButton : Button = binding.profileEditProfileBtn
        editProfileButton!!.setOnClickListener {
            Toast.makeText(this@ProfileFragment.requireContext(), "Edición de perfil realizada", Toast.LENGTH_SHORT).show()
//            val intLike = Intent(activity, MainActivity::class.java)
//            startActivity(intLike)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}