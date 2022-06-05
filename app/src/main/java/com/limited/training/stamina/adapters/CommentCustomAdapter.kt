package com.limited.training.stamina.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListAdapter
import android.widget.TextView
import com.limited.training.stamina.R
import com.limited.training.stamina.Util.Funciones.Companion.obtenerHoraString
import com.limited.training.stamina.objects.Comentario


class CommentCustomAdapter(var list: List<Comentario>?, var context: Context) : BaseAdapter(),
        ListAdapter {

        override fun getCount(): Int {
            return list?.size ?: 0;
        }

        override fun getItem(p0: Int): Any {
            return list?.get(p0) ?: "Error"
        }

        override fun getItemId(p0: Int): Long {
            return 0
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
            var view: View? = p1

            if (view == null) {
                val inflater =
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                view = inflater.inflate(R.layout.component_comment_button, null)
            }

            val tvMensaje = view!!.findViewById<View>(R.id.tvMensaje) as TextView
            tvMensaje.text = list?.get(p0)?.mensaje ?: "Error";
            val tvHora = view!!.findViewById<View>(R.id.tvHora) as TextView
            val hora = obtenerHoraString(list?.get(p0)?.fecha)
            tvHora.text = hora ?: "Error";



            return view
        }
}