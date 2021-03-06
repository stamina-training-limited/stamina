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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.objects.Usuario
import com.squareup.picasso.Picasso
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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

        //Obtenemos la hora que viene como timestamp en formato date
        fun obtenerHoraString (tiempo: Long?) : String{

            val stamp = Timestamp(tiempo?.toLong() ?: 0)
            val sdf = SimpleDateFormat("dd/MM/yy hh:mm")
            val dateStamp = Date(stamp.time)
            val date =sdf.format(dateStamp)
            val stringDate = date.toString()

            return stringDate
        }

        fun seguirUsuario(emailSeguido : String, emailSeguidor: String ){

        }

        fun dejarDeSeguirUsuario(emailSeguido : String, emailSeguidor: String ){

        }

        fun usuarioSigueAUsuario (emailSeguido : String, emailSeguidor: String ){

        }

        //Convierte metros por segundo a kilómetros por hora
        fun mpsToKph(mps: Float): Float{
            val speed = mps * 3.6F
            return speed
        }

        //
        fun getTimeStringBetweenElapsedMillis(startTime: Long, endTime: Long): String{
            val time = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(endTime - startTime),
                TimeUnit.MILLISECONDS.toMinutes(endTime - startTime) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(endTime - startTime)),
                TimeUnit.MILLISECONDS.toSeconds(endTime - startTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime - startTime)));

            return  time
        }
    }
}