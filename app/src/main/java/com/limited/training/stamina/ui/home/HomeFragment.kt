package com.limited.training.stamina.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.limited.training.stamina.Util.Utilidades

import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.FragmentHomeBinding


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
        var util : Utilidades = Utilidades(0, 1)
        listView.adapter = PublicationsCustoAdapter(routes, requireActivity().applicationContext,
        this, util.FLAG_HOME)
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}