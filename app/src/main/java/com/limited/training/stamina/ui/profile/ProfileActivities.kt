package com.limited.training.stamina.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.FragmentProfileActivitiesBinding
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.ui.home.HomeViewModel

class ProfileActivities : Fragment() {

    private var _binding: FragmentProfileActivitiesBinding? = null
    private val model: ProfileActivitiesViewModel by activityViewModels()

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

        var database = Funciones.recuperarReferenciaBBDD(requireActivity())
        var dbRef  = database.getReference("publicaciones")
        var pubs : HashMap<String, Publication> = hashMapOf()
        val datosGoogle = Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val emailUsuario = datosGoogle?.email


        if(dbRef != null) {

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    pubs = dataSnapshot.getValue<HashMap<String, Publication>>()!!
                    val pubsPropias = soloPublicacionesPropias(pubs, emailUsuario)

                    var listView: ListView = binding.listPublications
                    listView.adapter = PublicationsCustoAdapter(model,pubsPropias.values.toList(), requireActivity().applicationContext, emailUsuario!!)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        return root

    }

    fun soloPublicacionesPropias(pubs : HashMap<String, Publication>, emailUsuario : String?):
            HashMap<String, Publication> {

        var pubsPropias : HashMap<String, Publication> = hashMapOf()

        pubs.asSequence().filter { it.value.usuario == emailUsuario }.forEach { pubsPropias.put(it.key, it.value) }

        return pubsPropias

    }

}