package com.limited.training.stamina.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones
import com.limited.training.stamina.Util.Utilidades
import com.limited.training.stamina.objects.Publication
import com.limited.training.stamina.objects.Usuario
import org.w3c.dom.Text
import java.util.concurrent.TimeUnit

class UsersCustomAdapter(var list: List<Usuario>, var context: Context) : BaseAdapter(),
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
            view = inflater.inflate(R.layout.component_user_card, null)
        }

        val user_name: TextView = view!!.findViewById(R.id.user_card_name)
        val user_description: TextView = view!!.findViewById(R.id.user_card_description)
        val user_pic: ImageView = view!!.findViewById(R.id.user_card_pic)
        val follow_button: ImageButton = view!!.findViewById(R.id.user_card_follow_button)
        val got_to_profile: ImageButton = view!!.findViewById(R.id.user_card_go_to_profile_button)

        Funciones.establecerFotoPerfil(list[p0].urlFotoPerfil, user_pic)
        user_name.text = list[p0].nombre
        user_description.text = list[p0].descripcion

        follow_button!!.setOnClickListener {
            Toast.makeText(context, "Has empezado a seguir a ${list[p0].nombre}", Toast.LENGTH_SHORT).show()
        }

        got_to_profile!!.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigation_users_to_concreteUserPageFragment);
        }

        return view!!
    }

}