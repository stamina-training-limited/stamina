package com.limited.training.stamina.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.limited.training.stamina.MainActivity
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.objects.Usuario

class RegistrarUsuario : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_usuario)

        // Se procede a recuperar el correo del usuario que se ha autenticado
        var cuentaDeGoogle = Funciones.recuperarDatosCuentaGoogle(this)
        val correoUsuario = cuentaDeGoogle?.email

        // Se eliminan espacios para machear contra la clave en BBDD
        val correoUsuarioProcesado = Funciones.remplazarPuntos(correoUsuario!!)

        // Se procede a comprobar si el usuario que acaba de realizar el login existe en BBDD
        var database = Funciones.recuperarReferenciaBBDD(this)
        var referenciaUsuariosBBDD = database.getReference("usuarios")

        referenciaUsuariosBBDD.child(correoUsuarioProcesado).get().addOnSuccessListener {
            if (it.exists()){
                // Si existe, se pasa a la pantalla principal
                val intLogin : Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intLogin)

            } else {

                // Si no existe, se crea en BBDD y luego se procede a continuar a la pantalla principal
                val nuevoUsuario =
                    Usuario(
                        cuentaDeGoogle?.email.toString(),
                        cuentaDeGoogle?.displayName + cuentaDeGoogle?.familyName,
                        "Deportista novato",
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        emptyList(),
                        obtenerLinkFotoPerfil(cuentaDeGoogle?.photoUrl)
                    )

                // Si se guarda en BBDD, se continua a la siguiente pantalla
                referenciaUsuariosBBDD.
                child(correoUsuarioProcesado).setValue(nuevoUsuario).addOnSuccessListener {
                    val intLogin : Intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intLogin)
                }
            }
        }.addOnFailureListener{
            // TODO lanzar excepción si falla conexión contra BBDD
        }

    }

    fun obtenerLinkFotoPerfil(uri : Uri?) : String? {
        var resultado : String? = ""

        if(uri != null){
            resultado = uri.toString()
        }

        return resultado
    }
}