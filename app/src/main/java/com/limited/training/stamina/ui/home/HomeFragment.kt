package com.limited.training.stamina.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.Utilidades

import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.objects.Publication


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val model: HomeViewModel by activityViewModels()
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
        var database = Funciones.recuperarReferenciaBBDD(requireActivity())
        var dbRef  = database.getReference("publicaciones")
        var pubs : HashMap<String, Publication> = hashMapOf()

        if(dbRef != null) {

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    pubs = dataSnapshot.getValue<HashMap<String, Publication>>()!!
                    var listView: ListView = binding.listPublications
                    listView.adapter = PublicationsCustoAdapter(model,pubs.values.toList(), requireActivity().applicationContext)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}