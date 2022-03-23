package com.limited.training.stamina.ui.routes

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val users: List<String> = arrayOf("Ruta1", "Ruta2", "Ruta3", "Ruta4").toList()
        val listView: ListView = binding.listView
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireActivity().applicationContext,
            R.layout.simple_expandable_list_item_1,
            users
        )
        listView.adapter = adapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}