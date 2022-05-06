package com.limited.training.stamina.ui.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.adapters.UsersCustomAdapter
import com.limited.training.stamina.databinding.FragmentUsersBinding
import com.limited.training.stamina.objects.Usuario


class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private val model : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var database = Funciones.recuperarReferenciaBBDD(requireActivity())
        var dbRef  = database.getReference("usuarios")

        val searchView: SearchView = binding.searchView
        val listView: ListView = binding.listView
        var users: HashMap<String, Usuario> = hashMapOf()

        if(dbRef != null) {

            dbRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    users =  dataSnapshot.getValue<HashMap<String, Usuario>>()!!
                    println(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(text: String): Boolean {
                    var aux = users.filterKeys { it.contains(text) }
                    listView.adapter = UsersCustomAdapter(aux.values.toList(), requireActivity().applicationContext, model)
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    var aux = users.filterKeys { it.contains(newText) }
                    listView.adapter = UsersCustomAdapter(aux.values.toList(), requireActivity().applicationContext, model)

                    return false
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