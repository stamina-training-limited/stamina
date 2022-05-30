package com.limited.training.stamina.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
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
import com.limited.training.stamina.ui.home.HomeViewModel
import com.squareup.picasso.Picasso
import java.util.concurrent.TimeUnit


class PublicationsCustoAdapter(var model: SelectViewModel, var list: List<Publication>, var context: Context, var emailUsuarioActual : String) : BaseAdapter(),
    ListAdapter {

    var util : Utilidades = Utilidades(0, 1)

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
                    // TODO Evitar que se pueda conseguir un usuario nulo. Siempre al logearse por primera vez añadir a BBDD el usuario
                    usuario = dataSnapshot.getValue<Usuario>()!!
                    println("@@## > {$usuario}")

                    Funciones.establecerFotoPerfil(usuario.urlFotoPerfil, foto)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        // Dar me gusta a una publicación

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

            // Para que funcione tanto desde perfil como desde home, se comprueba desde que pantalla se viene y se añade

            model.select(list[p0])
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_home_comment);
        }

        shareButton!!.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(context, shareIntent, null)
        }

        return view!!
    }

}