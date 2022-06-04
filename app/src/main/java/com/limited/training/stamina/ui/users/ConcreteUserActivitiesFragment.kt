package com.limited.training.stamina.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.ComponentConcreteUserActivitiesBinding
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.objects.Usuario

class ConcreteUserActivitiesFragment: Fragment() {
    private var _binding: ComponentConcreteUserActivitiesBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ComponentConcreteUserActivitiesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val model : UserViewModel by activityViewModels()
        val actualUser: Usuario? = model.selected_u.value

        val appUser : GoogleSignInAccount? =
            Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val appUserMail = appUser?.email

        val database = Funciones.recuperarReferenciaBBDD(requireActivity())
        val dbRef  = database.getReference("publicaciones")
        var pubs: HashMap<String, Publication>

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                pubs = dataSnapshot.getValue<HashMap<String, Publication>>()!!
                val selectedPubs = onlyUserPublications(pubs, actualUser?.correo)

                val listView: ListView = binding.listPublications
                listView.adapter = PublicationsCustoAdapter(model,selectedPubs.values.toList(), requireActivity().applicationContext, appUserMail!!)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        return root
    }

    fun onlyUserPublications(pubs : HashMap<String, Publication>, userMail : String?):
            HashMap<String, Publication> {

        val selectedPubs : HashMap<String, Publication> = hashMapOf()

        pubs.asSequence().filter { it.value.usuario == userMail }.forEach { selectedPubs.put(it.key, it.value) }

        return selectedPubs

    }
}