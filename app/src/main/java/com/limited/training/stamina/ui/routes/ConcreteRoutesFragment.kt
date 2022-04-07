package com.limited.training.stamina.ui.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.limited.training.stamina.databinding.FragmentRouteConcreteBinding


class ConcreteRoutesFragment : Fragment() {

    private var _binding: FragmentRouteConcreteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(RoutesViewModel::class.java)

        _binding = FragmentRouteConcreteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val progressRouteButton : Button = binding.routeConcreteStartBtn
        progressRouteButton!!.setOnClickListener {
            Navigation.findNavController(root).navigate(com.limited.training.stamina.R.id.action_navigation_concrete_routes_to_navigation_progress_routes);
        }

        val classificationRouteButton : Button = binding.routeConcreteRatingBtn
        classificationRouteButton!!.setOnClickListener {
            Navigation.findNavController(root).navigate(com.limited.training.stamina.R.id.action_navigation_concrete_routes_to_navigation_classification_routes);
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}