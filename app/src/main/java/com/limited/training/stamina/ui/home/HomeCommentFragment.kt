package com.limited.training.stamina.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.databinding.FragmentHomeCommentBinding


class HomeCommentFragment : Fragment() {

    private var _binding: FragmentHomeCommentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCommentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Navigation.findNavController(root).navigate(R.id.action_navigation_home_comment_to_navigation_home);
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}