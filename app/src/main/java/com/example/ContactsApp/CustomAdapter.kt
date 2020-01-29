package com.example.ContactsApp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_layout.view.*

class CustomAdapter(val contactList: ArrayList<Contact>, val context: Context) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {

        val v = LayoutInflater.from(context).inflate(R.layout.list_layout, p0, false)


        return ViewHolder(v)


    }


    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
//        bindeamos los datos de nombre y email
        val name = contactList[p1].name
        p0.tv_contactname.text = name

        val email = contactList[p1].email
        p0.tv_contactemail.text = email
//        Listener de celda
        p0.itemView.setOnClickListener {
            context.startActivity(Intent(context, DetalleContactoActivity::class.java).apply {
                putExtra("name", name)
            })
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //           pasar datos a pantalla
        val tv_contactname = itemView.tv_contactName
        val tv_contactemail = itemView.tv_contactEmail
    }
}