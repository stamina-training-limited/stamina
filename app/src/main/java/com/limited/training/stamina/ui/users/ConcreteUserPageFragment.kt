package com.limited.training.stamina.ui.users

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
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

        if (usuario != null) {
            seteoCamposUsuario(_binding!!, usuario)
        }

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

        val followUnfollowButton : Button = binding.profileFollowUnfollowButton
        followUnfollowButton!!.setOnClickListener {
            //TODO
        }


        // Recuperacion de datos de usuario y seteo de los mismos de BBDD

        // Se recupera el email del usuario para buscar sus datos en BBDD
        val cuentaGoogle = Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val personEmail = cuentaGoogle?.email
        val personProfileImageUrl = cuentaGoogle?.photoUrl

        // Se eliminan los puntos para poder usarlo de clave contra el JSON de BBDD
        var processedEmail : String = Funciones.remplazarPuntos(personEmail!!)

        var database = Funciones.recuperarReferenciaBBDD(requireActivity())

        var myRef = database.getReference("usuarios/" + processedEmail)

        if (myRef != null){
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun signOut(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        val mGoogleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) };

        mGoogleSignInClient?.signOut()?.addOnCompleteListener{
            val intent : Intent = Intent(context, MainScreen::class.java)
            startActivity(intent)
        }
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
}