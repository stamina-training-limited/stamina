package com.limited.training.stamina.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.adapters.CommentCustomAdapter
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.databinding.FragmentHomeCommentBinding
import com.limited.training.stamina.objects.Comentario
import com.limited.training.stamina.objects.Publication


class HomeCommentFragment : Fragment() {

    private var _binding: FragmentHomeCommentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var dbRef : DatabaseReference
    private lateinit var listener : ValueEventListener
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCommentBinding.inflate(inflater, container, false)
        val model: HomeViewModel by activityViewModels()
        val root: View = binding.root
        var publicacion = model.selected.value?.first
        val flagFragment = model.selected.value?.second
        var database = Funciones.recuperarReferenciaBBDD(requireActivity())
        dbRef  = database.getReference("publicaciones/" + publicacion!!.ref)


        val appUser : GoogleSignInAccount? =
            Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val appUserMail = appUser?.email

        listener = dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                publicacion = dataSnapshot.getValue<Publication>()!!
                var listView: ListView = binding.listComment
                listView.adapter = CommentCustomAdapter(publicacion!!.comentarios, requireActivity().applicationContext)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if(flagFragment == Utilidades.FLAG_HOME) {
                        Navigation.findNavController(root)
                            .navigate(R.id.action_navigation_home_comment_to_navigation_home);
                    }

                    if(flagFragment == Utilidades.FLAG_PERFIL) {
                        Navigation.findNavController(root)
                            .navigate(R.id.action_navigation_home_comment_to_navigation_profile_activities);
                    }
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val publishButton : Button = binding.publishBtn
        val nuevoComentario: EditText = binding.commentPt
        publishButton!!.setOnClickListener {

            publicarComentario(requireContext(), Funciones.remplazarPuntos(appUserMail!!),
                publicacion!!,nuevoComentario.text.toString())
            nuevoComentario.text.clear()

        }
        return root
    }
    private fun publicarComentario(context: Context, loggedUserMail: String,publicacion: Publication, mensaje: String) {
        if(mensaje==""){
            Toast.makeText(context,"Es necesario insertar un comentario", Toast.LENGTH_SHORT).show()
        }else {
            val newComentario = Comentario(loggedUserMail, System.nanoTime(), mensaje)
            val newCommentList = publicacion.comentarios + newComentario
            dbRef.child("comentarios").setValue(newCommentList)
       }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dbRef.removeEventListener(listener)
        _binding = null
    }
}