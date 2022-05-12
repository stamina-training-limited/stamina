package com.limited.training.stamina.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.activities.MainScreen
import com.limited.training.stamina.adapters.PublicationsCustoAdapter
import com.limited.training.stamina.adapters.RoutesCustomAdapter
import com.limited.training.stamina.databinding.FragmentProfileBinding
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.objects.Ruta
import com.limited.training.stamina.objects.Usuario
import com.limited.training.stamina.ui.home.HomeViewModel
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.lang.StringBuilder


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val model: HomeViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //If is a tablet this textview is null
        val activitiesTextView : TextView? = binding.profileActivitiesSectionTv
        if (activitiesTextView != null) {
            activitiesTextView.setOnClickListener {
                Navigation.findNavController(root).navigate(R.id.action_navigation_profile_to_navigation_profile_activities);
            }
        }else{
            var database = Funciones.recuperarReferenciaBBDD(requireActivity())
            var dbRef  = database.getReference("publicaciones")
            var pubs : List<Publication>

            if(dbRef != null) {

                dbRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        pubs = dataSnapshot.getValue<List<Publication>>()!!
                        var listView: ListView = binding.listPublications!!
                        listView.adapter = PublicationsCustoAdapter(model,pubs, requireActivity().applicationContext)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        val editProfileButton : Button = binding.profileEditProfileBtn
        editProfileButton!!.setOnClickListener {
            Navigation.findNavController(root).navigate(R.id.action_navigation_profile_to_edit_profile);
        }

        val logOutButton : Button = binding.profileLogOut
        logOutButton!!.setOnClickListener {
            signOut()
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
            var usuario : Usuario

            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    // TODO Evitar que se pueda conseguir un usuario nulo. Siempre al logearse por primera vez añadir a BBDD el usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!

                    // Seteo de los datos del usuario

                    val nombreUsuario: TextView = binding.profileNameTv
                    val descripcionUsuario: TextView = binding.profileUserDescriptionTv
                    val numeroSeguidores: TextView = binding.profileFollowersTv
                    val numeroSeguidos: TextView = binding.profileFollowingTv
                    val fotoPerfil: ImageView = binding.profilePicIv
                    val numeroActividades : TextView = binding.profileActivitiesTv
                    val numeroRutas : TextView = binding.profileRoutesTv
                    val numeroPublicaciones : TextView = binding.profilePublicationsTv

                    nombreUsuario.text = usuario.nombre
                    descripcionUsuario.text = usuario.descripcion
                    numeroSeguidores.text = usuario.seguidores.size.toString() + " Seguidores"
                    numeroSeguidos.text = usuario.seguidos.size.toString() + " Seguidos"
                    numeroActividades.text = usuario.actividades.size.toString()
                    numeroRutas.text = usuario.rutas.size.toString()
                    numeroPublicaciones.text = usuario.publicaciones.size.toString()

                    Funciones.establecerFotoPerfil(usuario.urlFotoPerfil, fotoPerfil)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w("loadPost:onCancelled", databaseError.toException())
                }
            })
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

        if (mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut()
                .addOnCompleteListener{
                    val intent : Intent = Intent(context, MainScreen::class.java)
                    startActivity(intent)
                }
        }
    }
}