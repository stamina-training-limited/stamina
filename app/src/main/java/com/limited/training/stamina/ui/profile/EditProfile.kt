package com.limited.training.stamina.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.databinding.FragmentEditProfileBinding
import com.limited.training.stamina.databinding.FragmentHomeBinding
import com.limited.training.stamina.databinding.FragmentProfileBinding
import com.limited.training.stamina.objects.Usuario

class EditProfile : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Se recupera referencia a BBDD
        var database = Funciones.recuperarReferenciaBBDD(requireActivity())
        var referenciaUsuariosBBDD = database.getReference("usuarios")

        // Se procede a recuperar el correo del usuario que se ha autenticado
        var cuentaDeGoogle = Funciones.recuperarDatosCuentaGoogle(requireActivity())
        val correoUsuarioProcesado = Funciones.remplazarPuntos(cuentaDeGoogle?.email!!)


        // Se guardan las referencias a los campos de texto y los botones
        val nuevoNombreUsuario: EditText = binding.editTextName
        val nuevaDescripcionUsuario: EditText = binding.editTextDesc
        val botonSincronizarFoto: Button = binding.syncGmail
        val botonConfirmarCambios: Button = binding.confirmEdition

        // Si se desea sincronizar la foto de Gmail
        botonSincronizarFoto!!.setOnClickListener {

            // Se recupera la URL de la foto de GMAIL
            val nuevaUrlFoto = cuentaDeGoogle?.photoUrl.toString()

            referenciaUsuariosBBDD.
            child(correoUsuarioProcesado).child("urlFotoPerfil").setValue(nuevaUrlFoto).addOnSuccessListener {
                Toast.makeText(requireActivity(), getString(R.string.sincronizar_foto_gmail_exito), Toast.LENGTH_SHORT).show();
            }
        }

        botonConfirmarCambios!!.setOnClickListener {

            // Si se desea actualizar nombre o descripcion
            val actualizaNombre: Boolean = nuevoNombreUsuario.text.toString().isNotEmpty()
            val actualizaDescripcion: Boolean = nuevaDescripcionUsuario.text.toString().isNotEmpty()

            if (actualizaNombre || actualizaDescripcion) {

                if(actualizaNombre){
                    referenciaUsuariosBBDD.
                    child(correoUsuarioProcesado).child("nombre").setValue(nuevoNombreUsuario.text.toString()).addOnSuccessListener {
                        Toast.makeText(requireActivity(), getString(R.string.nombre_actualizado), Toast.LENGTH_SHORT).show();
                    }
                }
                if(actualizaDescripcion){
                    referenciaUsuariosBBDD.
                    child(correoUsuarioProcesado).child("descripcion").setValue(nuevaDescripcionUsuario.text.toString()).addOnSuccessListener {
                        Toast.makeText(requireActivity(), getString(R.string.descripcion_actualizada), Toast.LENGTH_SHORT).show();
                    }
                }

            } else {
                Toast.makeText(requireActivity(), getString(R.string.campos_vacios), Toast.LENGTH_SHORT).show();
            }
        }

        return root

    }
}