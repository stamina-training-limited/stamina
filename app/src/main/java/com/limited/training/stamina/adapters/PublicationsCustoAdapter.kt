package com.limited.training.stamina.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.SelectViewModel
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.objects.Usuario
import java.util.concurrent.TimeUnit
import kotlin.text.StringBuilder


class PublicationsCustoAdapter(
    var model: SelectViewModel,
    var list: List<Publication>,
    var context: Context,
    var emailUsuarioActual: String,
    var flagFragment: Int
) : BaseAdapter(),
    ListAdapter {

    override fun getCount(): Int {
        return list.size;
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var view: View? = p1

        if (view == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.component_publication, null)
        }

        val likeButton: ImageButton = view!!.findViewById(R.id.PublicationEntry1Like_btn)
        val commentButton: ImageButton = view!!.findViewById(R.id.PublicationEntry1Comment_btn)
        val shareButton: ImageButton = view!!.findViewById(R.id.PublicationEntry1Share_btn)

        val textName: TextView = view!!.findViewById(R.id.feedEntry1Name_tv)
        val textDateAndLocation: TextView = view!!.findViewById(R.id.feedEntry1DateAndLocation_tv)
        val textTitle: TextView = view!!.findViewById(R.id.feedEntry1Title_tv)
        val textDistance: TextView = view!!.findViewById(R.id.feedEntry1Distance_tv)
        val ritmo: TextView = view!!.findViewById(R.id.feedEntry1Pace_tv)
        val tiempo: TextView = view!!.findViewById(R.id.feedEntry1Time_tv)
        val foto: ImageView = view!!.findViewById(R.id.feedEntry1profilePic_iv)
        val megustas: TextView = view!!.findViewById(R.id.megustas)

        textName.text = list[p0].nombre
        textDateAndLocation.text = list[p0].hora.plus(" - ").plus(list[p0].lugar)
        textTitle.text = list[p0].titulo
        textDistance.text = list[p0].distancia.toString().plus(" km")
        ritmo.text = list[p0].ritmo.toString().plus(" min/km")
        megustas.text = list[p0].megustas.size.toString()

        val millis: Long = list[p0].tiempo
        tiempo.text = String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));


        var database = Funciones.recuperarReferenciaBBDD(context)
        var myRef = database.getReference("usuarios/" + Funciones.remplazarPuntos(list[p0].usuario))

        if (myRef != null){
            var usuario : Usuario

            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get datos de usuario
                    // TODO Evitar que se pueda conseguir un usuario nulo. Siempre al logearse por primera vez a??adir a BBDD el usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!
                    println("@@## > {$usuario}")

                    Funciones.establecerFotoPerfil(usuario.urlFotoPerfil, foto)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        // Dar me gusta a una publicaci??n

        likeButton!!.setOnClickListener {
            val refBBDD = Funciones.recuperarReferenciaBBDD(context)
            val referenciaUsuariosBBDD = refBBDD.getReference("publicaciones")
            var numeroMegustas = referenciaUsuariosBBDD.child(list[p0].ref).child("megustas")
            var numeroMegustasUsuarios : List<String>?
            if (numeroMegustas != null)
            {
                numeroMegustas.get().addOnSuccessListener {
                    numeroMegustasUsuarios = it.value as List<String>?
                    if(numeroMegustasUsuarios == null){
                        numeroMegustasUsuarios = emptyList()
                    }
                    //Si el usuario no le ha dado a me gusta previamente, se le permite dar me gusta
                    if(!numeroMegustasUsuarios!!.contains(emailUsuarioActual)){
                        val nuevaListaMeGusta = numeroMegustasUsuarios!! + emailUsuarioActual
                        referenciaUsuariosBBDD.child(list[p0].ref).child("megustas").setValue(nuevaListaMeGusta)
                    }else{
                        val nuevaListaMeGusta = numeroMegustasUsuarios!! - emailUsuarioActual
                        referenciaUsuariosBBDD.child(list[p0].ref).child("megustas").setValue(nuevaListaMeGusta)
                    }
                }
            }
        }

        commentButton!!.setOnClickListener {

            // Para que funcione tanto desde perfil como desde home, se comprueba desde que pantalla se viene y se a??ade



            if(flagFragment == Utilidades.FLAG_HOME){
                val infoViewModel : Pair<Publication, Int> = Pair(list[p0], Utilidades.FLAG_HOME)
                model.select(infoViewModel)
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_home_comment);
            }

            if (flagFragment == Utilidades.FLAG_PERFIL){
                val infoViewModel : Pair<Publication, Int> = Pair(list[p0], Utilidades.FLAG_PERFIL)
                model.select(infoViewModel)
                Navigation.findNavController(view).navigate(R.id.action_navigation_profile_activities_to_navigation_home_comment);
            }

        }

        shareButton!!.setOnClickListener {

            val millis: Long = list[p0].tiempo
            val tiempoFormateado = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

            var sharedString : StringBuilder = StringBuilder()
            sharedString.append(context.getString(R.string.introResumenActividad))
            sharedString.append(" ")
            sharedString.append(context.getString(R.string.distancia))
            sharedString.append(" " + list[p0].distancia + " ")
            sharedString.append(context.getString(R.string.unidad_distancia))
            sharedString.append(" ")
            sharedString.append(context.getString(R.string.ritmo))
            sharedString.append(" " + list[p0].ritmo + " ")
            sharedString.append(context.getString(R.string.unidad_ritmo))
            sharedString.append(" ")
            sharedString.append(context.getString(R.string.tiempo))
            sharedString.append(" $tiempoFormateado. ")

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, sharedString.toString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(context, shareIntent, null)
        }

        return view!!
    }

}