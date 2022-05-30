package com.limited.training.stamina.ui.users

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.activities.MainScreen
import com.limited.training.stamina.databinding.FragmentConcreteUserPageBinding
import com.limited.training.stamina.objects.Usuario


class ConcreteUserPageFragment : Fragment() {

    private var _binding: FragmentConcreteUserPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConcreteUserPageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val model : UserViewModel by activityViewModels()
        var usuario : Usuario? = model.selected.value

        seteoCamposUsuario(_binding!!, usuario!!)

        //If is a tablet this textview is null
        val activitiesTextView : TextView? = binding.profileActivitiesSectionTv
        if (activitiesTextView != null) {
            activitiesTextView.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.action_navigation_profile_to_navigation_profile_activities);
            }
        }else{
            var database = Funciones.recuperarReferenciaBBDD(requireActivity())
            var dbRef  = database.getReference("publicaciones")

            if(dbRef != null) {

            }
        }

        // Se recupera el usuario logeado en la aplicación. Con el mail recuperaremos
        // la lista de usuarios que sigue.
        val usuarioAplicacion : GoogleSignInAccount? =
            Funciones.recuperarDatosCuentaGoogle(requireActivity())

        val emailUsuario = usuarioAplicacion?.email

        // Si el usuario actual no sigue al usuario concreto, el botón debería dar la
        // opción de dejar de seguir. En caso contrario, el botón debería permitir al usuario.
        comprobarSiUsuarioLogeadoSigue(requireContext(), Funciones.remplazarPuntos(emailUsuario!!),
                Funciones.remplazarPuntos(usuario.correo))

        val botonSeguir : Button = binding!!.profileFollow
        val botonDejarDeSeguir : Button = binding!!.profileUnfollow

        botonSeguir.setOnClickListener {
            seguirUsuario(requireContext(), Funciones.remplazarPuntos(emailUsuario!!),
                Funciones.remplazarPuntos(usuario.correo))

            Toast.makeText(context, getString(R.string.usuarioSeguidoCorrectamente),
                Toast.LENGTH_LONG).show();

        }

        botonDejarDeSeguir.setOnClickListener {
            dejarDeSeguir(requireContext(), Funciones.remplazarPuntos(emailUsuario!!),
                Funciones.remplazarPuntos(usuario.correo))

            Toast.makeText(context, getString(R.string.usuarioDejadoDeSeguirCorrectamente),
                Toast.LENGTH_LONG).show();
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun seteoCamposUsuario(binding: FragmentConcreteUserPageBinding, usuario : Usuario){
        // Seteo de los datos del usuario

        val nombreUsuario: TextView = binding.profileNameTv
        val descripcionUsuario: TextView = binding.profileUserDescriptionTv
        val numeroSeguidores: TextView = binding.profileFollowersTv
        val numeroSeguidos: TextView = binding.profileFollowingTv
        val fotoPerfil: ImageView = binding.profilePicIv
        val numeroActividades : TextView = binding.profileActivitiesTv
        val numeroPublicaciones : TextView = binding.profilePublicationsTv

        nombreUsuario.text = usuario.nombre
        descripcionUsuario.text = usuario.descripcion
        numeroSeguidores.text = usuario.seguidores.size.toString() + " Seguidores"
        numeroSeguidos.text = usuario.seguidos.size.toString() + " Seguidos"
        numeroActividades.text = usuario.actividades.size.toString()
        numeroPublicaciones.text = usuario.publicaciones.size.toString()

        Funciones.establecerFotoPerfil(usuario.urlFotoPerfil, fotoPerfil)

    }

    fun seguirUsuario(context: Context, emailUsuarioLogeado: String, emailUsuarioConcreto: String){
        var database = Funciones.recuperarReferenciaBBDD(context)

        // Añadir a la lista del ususario logeado el usuario que sigue
        var referenciaUsuarioLogeadoBBDD = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(emailUsuarioLogeado))
        var usuario : Usuario

        if (referenciaUsuarioLogeadoBBDD != null){
            referenciaUsuarioLogeadoBBDD.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!

                    if(!usuario.seguidos.contains(emailUsuarioConcreto)) {
                        val nuevaListaSeguidos = usuario.seguidos + emailUsuarioConcreto
                        referenciaUsuarioLogeadoBBDD.child("seguidos")
                            .setValue(nuevaListaSeguidos)
                    }


                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        // Añadir a la lista del seguido el seguidor

        var referenciaUsuarioConcretoBBDD = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(emailUsuarioConcreto))
        var usuarioConcreto : Usuario

        if (referenciaUsuarioConcretoBBDD != null){
            referenciaUsuarioConcretoBBDD.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    usuarioConcreto = dataSnapshot.getValue<Usuario>()!!

                    if(!usuarioConcreto.seguidores.contains(emailUsuarioLogeado)) {
                        val nuevaListaSeguidores = usuarioConcreto.seguidores + emailUsuarioLogeado
                        referenciaUsuarioConcretoBBDD
                            .child("seguidores").setValue(nuevaListaSeguidores)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun dejarDeSeguir(context: Context, emailUsuarioLogeado: String, emailUsuarioConcreto: String){
        var database = Funciones.recuperarReferenciaBBDD(context)

        // Añadir a la lista del ususario logeado el usuario que sigue
        var referenciaUsuarioLogeadoBBDD = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(emailUsuarioLogeado))
        var usuario : Usuario

        if (referenciaUsuarioLogeadoBBDD != null){
            referenciaUsuarioLogeadoBBDD.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!

                    if(usuario.seguidos.contains(emailUsuarioConcreto)) {
                        val nuevaListaSeguidos = usuario.seguidos - emailUsuarioConcreto
                        referenciaUsuarioLogeadoBBDD.child("seguidos")
                            .setValue(nuevaListaSeguidos)
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        // Añadir a la lista del seguido el seguidor

        var referenciaUsuarioConcretoBBDD = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(emailUsuarioConcreto))
        var usuarioConcreto : Usuario

        if (referenciaUsuarioConcretoBBDD != null){
            referenciaUsuarioConcretoBBDD.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    usuarioConcreto = dataSnapshot.getValue<Usuario>()!!

                    if(usuarioConcreto.seguidores.contains(emailUsuarioLogeado)) {
                        val nuevaListaSeguidores = usuarioConcreto.seguidores - emailUsuarioLogeado
                        referenciaUsuarioConcretoBBDD
                            .child("seguidores").setValue(nuevaListaSeguidores)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    fun comprobarSiUsuarioLogeadoSigue(context : Context, emailUsuarioLogeado : String,
                                       emailUsuarioConcreto : String){

        var database = Funciones.recuperarReferenciaBBDD(context)
        var myRef = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(emailUsuarioLogeado))
        var usuario : Usuario

        if (myRef != null){
            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!

                    if(usuario.seguidos.contains(emailUsuarioConcreto))
                    {
                        binding.profileUnfollow.visibility = View.VISIBLE
                        binding.profileFollow.visibility = View.INVISIBLE
                    }else{
                        binding.profileFollow.visibility = View.VISIBLE
                        binding.profileUnfollow.visibility = View.INVISIBLE
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }



    }
}