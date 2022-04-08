package com.limited.training.stamina.ui.routes

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.limited.training.stamina.adapters.RoutesCustomAdapter
import com.limited.training.stamina.databinding.FragmentRoutesBinding


class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesBinding? = null

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

        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val routes: ArrayList<String> = arrayListOf(
            "Ruta sierra morena",
            "Ruta mortal me morí",
            "Esta era la facilita",
            "Aquí me caí",
            "La de la viaja entrañable",
            "Tramo camino de santiago",
            "Donde la zona de petting",
            "Dura de carallo",
            "Monte a través"
        )
        val listView: ListView = binding.listView

        listView.adapter = RoutesCustomAdapter(routes, requireActivity().applicationContext)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}