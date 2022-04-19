package com.limited.training.stamina.ui.routes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.limited.training.stamina.adapters.RoutesCustomAdapter
import com.limited.training.stamina.databinding.FragmentRoutesBinding
import com.limited.training.stamina.objects.Ruta
import java.nio.channels.Selector


class RoutesFragment : Fragment() {

    private var _binding: FragmentRoutesBinding? = null
    private val model: RoutesViewModel by activityViewModels()


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val notificationsViewModel =
//            ViewModelProvider(this).get(RoutesViewModel::class.java)

        _binding = FragmentRoutesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        var database= FirebaseDatabase.getInstance("https://stamina-training-default-rtdb.europe-west1.firebasedatabase.app/")
        var myRef = database.getReference("Rutas")
        var routes = listOf<Ruta>()

        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Rutas
                routes = dataSnapshot.getValue<List<Ruta>>()!!
                val listView: ListView = binding.listView

                listView.adapter = RoutesCustomAdapter(model,routes, requireActivity().applicationContext)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        })

//        val routes: ArrayList<String> = arrayListOf(
//            "Ruta sierra morena",
//            "Ruta mortal me morí",
//            "Esta era la facilita",
//            "Aquí me caí",
//            "La de la viaja entrañable",
//            "Tramo camino de santiago",
//            "Donde la zona de petting",
//            "Dura de carallo",
//            "Monte a través"
//        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}