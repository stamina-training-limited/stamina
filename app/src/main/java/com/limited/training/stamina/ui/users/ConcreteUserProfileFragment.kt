package com.limited.training.stamina.ui.users

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.databinding.ComponentConcreteUserProfileBinding
import com.limited.training.stamina.objects.Usuario

class ConcreteUserProfileFragment: Fragment() {

    private var _binding: ComponentConcreteUserProfileBinding? = null
    private lateinit var _cUser: Usuario
    private lateinit var _lUser: Usuario
    private lateinit var _lUserRef: DatabaseReference
    private lateinit var _lUserRefListener: ValueEventListener

    private lateinit var _cUserRef: DatabaseReference
    private lateinit var _cUserRefListener: ValueEventListener

    private lateinit var _myRef: DatabaseReference
    private lateinit var _myRefListener: ValueEventListener

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ComponentConcreteUserProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val model : UserViewModel by activityViewModels()
        val actualUser: Usuario? = model.selected_u.value

        setUserData(_binding!!, actualUser!!)

        val appUser : GoogleSignInAccount? =
            Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val appUserMail = appUser?.email

        checkIfUserFollowsUser(requireContext(), Funciones.remplazarPuntos(appUserMail!!),
            Funciones.remplazarPuntos(actualUser.correo))


        val followButton : Button = binding.profileFollow
        val unfollowButton : Button = binding.profileUnfollow

        followButton.setOnClickListener {
            follow(requireContext(), Funciones.remplazarPuntos(appUserMail),
                Funciones.remplazarPuntos(actualUser.correo))
        }

        unfollowButton.setOnClickListener {
            unfollow(requireContext(), Funciones.remplazarPuntos(appUserMail),
                Funciones.remplazarPuntos(actualUser.correo))
        }

        val database = Funciones.recuperarReferenciaBBDD(requireContext())
        _lUserRef = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(appUserMail))

        _lUserRefListener = _lUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _lUser = dataSnapshot.getValue<Usuario>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        _cUserRef = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(actualUser.correo))

        _cUserRefListener = _cUserRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _cUser = dataSnapshot.getValue<Usuario>()!!
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


        return root
    }

    private fun setUserData(binding: ComponentConcreteUserProfileBinding, usuario : Usuario) {
        val userName: TextView = binding.profileNameTv
        val userDescription: TextView = binding.profileUserDescriptionTv
        val numberFollowers: TextView = binding.profileFollowersTv
        val numberFollowing: TextView = binding.profileFollowingTv
        val profilePic: ImageView = binding.profilePicIv
        val numberActivities : TextView = binding.profileActivitiesTv
        val numberPublications : TextView = binding.profilePublicationsTv

        userName.text = usuario.nombre
        userDescription.text = usuario.descripcion
        numberFollowers.text = usuario.seguidores.size.toString() + " Seguidores"
        numberFollowing.text = usuario.seguidos.size.toString() + " Seguidos"
        numberActivities.text = usuario.actividades.size.toString()
        numberPublications.text = usuario.publicaciones.size.toString()

        Funciones.establecerFotoPerfil(usuario.urlFotoPerfil, profilePic)
    }

    private fun follow(context: Context, loggedUserMail: String, concreteUserMail: String) {

        if(!_lUser.seguidos.contains(concreteUserMail)) {
            val newFollowingList = _lUser.seguidos + concreteUserMail
            _lUserRef.child("seguidos").setValue(newFollowingList)
        } else {
            print("Error, no debería ser posible")
            return
        }

        if(!_cUser.seguidos.contains(loggedUserMail)) {
            val newFollowingList = _cUser.seguidos + loggedUserMail
            _cUserRef.child("seguidos").setValue(newFollowingList)
        } else {
            print("Error, no debería ser posible")
            return
        }

        Toast.makeText(context, getString(R.string.usuarioSeguidoCorrectamente),
            Toast.LENGTH_LONG).show()
    }

    private fun unfollow(context: Context, loggedUserMail: String, concreteUserMail: String) {

        if(_lUser.seguidos.contains(concreteUserMail)) {
            val newFollowingList = _lUser.seguidos - concreteUserMail
            _lUserRef.child("seguidos").setValue(newFollowingList)
        } else {
            print("Error, no debería ser posible")
            return
        }

        if(_cUser.seguidos.contains(loggedUserMail)) {
            val newFollowingList = _cUser.seguidos - loggedUserMail
            _cUserRef.child("seguidos").setValue(newFollowingList)
        } else {
            print("Error, no debería ser posible")
            return
        }

        Toast.makeText(context, getString(R.string.usuarioDejadoDeSeguirCorrectamente),
            Toast.LENGTH_LONG).show()
    }


    private fun checkIfUserFollowsUser(context : Context, loggedUserMail : String,
                                       concreteUserMail : String){
        val database = Funciones.recuperarReferenciaBBDD(context)
        _myRef = database.getReference("usuarios/"
                + Funciones.remplazarPuntos(loggedUserMail))
        var usuario : Usuario

        _myRefListener = _myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                usuario = dataSnapshot.getValue<Usuario>()!!

                if(usuario.seguidos.contains(concreteUserMail))
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        if(this::_lUserRef.isInitialized) _lUserRef.removeEventListener(_lUserRefListener)
        if(this::_cUserRef.isInitialized) _cUserRef.removeEventListener(_cUserRefListener)
        if(this::_myRef.isInitialized) _myRef.removeEventListener(_myRefListener)
    }

}