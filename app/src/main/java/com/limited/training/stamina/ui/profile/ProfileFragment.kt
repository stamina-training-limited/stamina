package com.limited.training.stamina.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.databinding.FragmentProfileBinding

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
            Navigation.findNavController(root).navigate(R.id.action_navigation_profile_to_navigation_profile_activities);
        }

        val editProfileButton : Button = binding.profileEditProfileBtn
        editProfileButton!!.setOnClickListener {
            Toast.makeText(this@ProfileFragment.requireContext(), "Edición de perfil realizada", Toast.LENGTH_SHORT).show()

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}