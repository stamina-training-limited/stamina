package com.limited.training.stamina.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.FragmentProfileActivitiesBinding

class ProfileActivities : Fragment() {

    private var _binding: FragmentProfileActivitiesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Cambio a vista de detalles de perfil
        val activitiesTextView : TextView = binding.activitiesProfileSectionTv
        activitiesTextView!!.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_profile_activities_to_navigation_profile);
        }

        val routes: ArrayList<String> = arrayListOf("Ruta1", "Ruta2", "Ruta3", "Ruta4", "Ruta5", "Ruta6", "Ruta7", "Ruta8", "Ruta9", "Ruta10", "Ruta11")
        val listView: ListView = binding.listPublications
        var util : Utilidades = Utilidades(0, 1)
        listView.adapter = PublicationsCustoAdapter(routes, requireActivity().applicationContext,
            this, util.FLAG_PERFIL
        )

        return root

    }

}