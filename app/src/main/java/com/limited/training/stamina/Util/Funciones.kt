package com.limited.training.stamina.Util

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.FirebaseDatabase
import com.limited.training.stamina.R

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
    }
}