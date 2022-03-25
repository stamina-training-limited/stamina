package com.limited.training.stamina.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.adapters.RoutesCustomAdapter
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.databinding.FragmentRoutesBinding
import com.limited.training.stamina.feed.FeedComment
import com.limited.training.stamina.feed.FeedLike
import com.limited.training.stamina.feed.FeedShare
import com.limited.training.stamina.ui.routes.RoutesViewModel

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



        val routes: ArrayList<String> = arrayListOf("Ruta1", "Ruta2", "Ruta3", "Ruta4", "Ruta5", "Ruta6", "Ruta7", "Ruta8", "Ruta9", "Ruta10", "Ruta11")
        val listView: ListView = binding.listPublications

        listView.adapter = PublicationsCustoAdapter(routes, requireActivity().applicationContext)
        return root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}