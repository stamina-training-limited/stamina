package com.limited.training.stamina.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.objects.Publication



class PublicationsCustoAdapter(var list: List<Publication>, var context: Context) : BaseAdapter(),
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

        textName.text = list[p0].nombre
        textDateAndLocation.text = list[p0].hora.plus(" - ").plus(list[p0].lugar)
        textTitle.text = list[p0].titulo
        textDistance.text = list[p0].distancia.toString().plus("km")
        ritmo.text = list[p0].ritmo.toString().plus("min/km")
        tiempo.text = list[p0].tiempo.toString()


        likeButton!!.setOnClickListener {
            Toast.makeText(context, "Me gusta mucho", Toast.LENGTH_SHORT).show()
        }
        commentButton!!.setOnClickListener {

            // Para que funcione tanto desde perfil como desde home, se comprueba desde que pantalla se viene y se añade

 //          if(padre == util.FLAG_PERFIL){
 //              ref.parentFragmentManager.commit {
 //                  replace<ProfileActivities>(R.id.nav_host_fragment_activity_main)
 //                  setReorderingAllowed(true)
 //                  addToBackStack("name") // name can be null
 //              }
 //          }

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