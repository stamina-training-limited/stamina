package com.limited.training.stamina.Util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.ImageView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.FirebaseDatabase
import com.limited.training.stamina.R
import com.squareup.picasso.Picasso

class Funciones {

    companion object{

        fun remplazarPuntos(entrada: String): String {
            return entrada.replace(".", "")
        }

        fun recuperarDatosCuentaGoogle(actividad : Activity) : GoogleSignInAccount? {

            val acct = GoogleSignIn.getLastSignedInAccount(actividad)
            if (acct == null) {
                Log.println(Log.ERROR, "Correo", "Correo electrónico nulo");
            }

            return acct
        }

        // Recupera el datasource de la base de datos. Utiliza la URL guardada en el
        // archivo de configuración
        fun recuperarReferenciaBBDD(context: Context): FirebaseDatabase {
            val URL_FIREBASE = context.getString(R.string.datasource_firebase)
            return FirebaseDatabase.getInstance(URL_FIREBASE)
        }

        fun establecerFotoPerfil(urlImagen : String?, imageView : ImageView){

            // Se procede a comprobar si la imagen viene vacía o no. Si viene vacía, se deja la imagen
            // por defecto. Si no, se establece la foto de gmail.
            if(urlImagen == null || urlImagen.equals("")){
                return
            }

            // Si la carga es fructuosa se actualiza, si no se pone la foto por defecto
            val uriImagen : Uri = Uri.parse(urlImagen)

            Picasso.get()
                .load(uriImagen)
                .placeholder(R.drawable.google_perfil)
                .error(R.drawable.google_perfil)
                .into(imageView);
        }


    }
}