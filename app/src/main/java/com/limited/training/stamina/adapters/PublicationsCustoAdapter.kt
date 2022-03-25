package com.limited.training.stamina.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.limited.training.stamina.R

class PublicationsCustoAdapter(var list: ArrayList<String>, var context: Context) : BaseAdapter(),
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

        likeButton!!.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_home_like);
        }
        commentButton!!.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_home_comment);
        }
        shareButton!!.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_home_share);
        }

        return view!!
    }

}