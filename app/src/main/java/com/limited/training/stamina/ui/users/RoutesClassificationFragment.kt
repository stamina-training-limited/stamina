package com.limited.training.stamina.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.limited.training.stamina.adapters.RoutesClassificationCustomAdapter
import com.limited.training.stamina.databinding.FragmentRouteClassificationBinding


class RoutesClassificationFragment : Fragment() {

    private var _binding: FragmentRouteClassificationBinding? = null

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

        _binding = FragmentRouteClassificationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val classifications: ArrayList<String> = arrayListOf("\t\t1º\t\t40:07:55\t\t\tManuel Couto", "\t\t2º\t\t42:12:33\t\t\tJosé Figueiras", "\t\t3º\t\t55:15:54\t\t\tAna Pan")
        val listView: ListView = binding.routeClassificationLv

        listView.adapter = RoutesClassificationCustomAdapter(classifications, requireActivity().applicationContext)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}