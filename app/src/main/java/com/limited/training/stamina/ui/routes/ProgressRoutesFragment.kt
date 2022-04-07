package com.limited.training.stamina.ui.routes

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.limited.training.stamina.activities.PausedActivity
import com.limited.training.stamina.activities.ProgressActivity
import com.limited.training.stamina.databinding.FragmentRouteProgressBinding


class ProgressRoutesFragment : Fragment() {

    private var _binding: FragmentRouteProgressBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteProgressBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val stopButton: Button = binding.routeProgressStopBtn
        stopButton!!.setOnClickListener {
            val intActivityInProgress: Intent = Intent(activity, PausedActivity::class.java)
            startActivity(intActivityInProgress)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}