package com.limited.training.stamina.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.limited.training.stamina.R
import com.limited.training.stamina.objects.Ruta
import com.limited.training.stamina.ui.routes.RoutesViewModel


class RoutesCustomAdapter(var model: RoutesViewModel,var list: List<Ruta>, var context: Context) : BaseAdapter(), ListAdapter {

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
            view = inflater.inflate(R.layout.component_route_button, null)
        }

        val tvContact = view!!.findViewById<View>(R.id.tvContact) as TextView
        tvContact.text = list[p0].nombre;

        val callbtn: Button = view.findViewById(R.id.btn)
        callbtn.setOnClickListener {
            model.select(list[p0])
            Navigation.findNavController(view).navigate(R.id.action_navigation_routes_to_navigation_concrete_routes);
        }

        return view
    }

}