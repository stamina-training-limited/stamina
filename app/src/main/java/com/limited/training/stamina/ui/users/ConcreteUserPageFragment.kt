package com.limited.training.stamina.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.limited.training.stamina.R
import com.limited.training.stamina.databinding.FragmentConcreteUserPageBinding


class ConcreteUserPageFragment : Fragment() {

    private var _binding: FragmentConcreteUserPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConcreteUserPageBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val activitiesTextView : TextView = binding.profileActivitiesSectionTv
        val profileTextView: TextView = binding.profileSectionTv


        val userProfile: Fragment = ConcreteUserProfileFragment()
        val userActivities: Fragment = ConcreteUserActivitiesFragment()

        var transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.concreteUser_fragmentLayout, userProfile).commit()

        activitiesTextView.setOnClickListener {
            transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.concreteUser_fragmentLayout, userActivities).commit()
        }

        profileTextView.setOnClickListener{
            transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.concreteUser_fragmentLayout, userProfile).commit()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}