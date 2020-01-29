package com.example.ContactsApp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_contacts_list.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ContactsListActivity : AppCompatActivity() {
    lateinit var db: AppDatabase
    var contactos: ArrayList<Contact> = ArrayList()

    lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts_list)
//cargamos contactos
        loadContacts()
        Thread.sleep(500)
//        configuramos la vista
        configView()


    }

    private fun loadContacts() {
//        instanciamos DB y recuperamos los registros con la query
        db = AppDatabase.getInstance(applicationContext)!!
        GlobalScope.launch {
            //                recuperamos todos los contactos
            contactos = db.contactDao().loadAllContacts().toCollection(ArrayList())
        }
    }

    private fun configView() {
        recyclerView.adapter = CustomAdapter(contactos, this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                DividerItemDecoration.VERTICAL
            )
        )

    }
//    sobreescribimos este metodo para mostrar nuestro menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        return true
    }
//    sobreescribimos el metodo para tratar las opciones seleccionadas del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean =// Handle item selection
        when (item.itemId) {
            R.id.item_menu_1/*Crear Contacto*/ -> {

                val intentToCreate = Intent(this, CrearContactoActivity::class.java)
                startActivity(intentToCreate)
                finish()

                true
            }
            R.id.item_menu_2/*Cerrar  Sesión.*/ -> {
                val intentToLogin = Intent(this@ContactsListActivity, ActivityLogin::class.java)
                startActivity(intentToLogin)
                finish()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    override fun onBackPressed() {
        val backIntent = Intent(this, ActivityLogin::class.java)
        startActivity(backIntent)
    }
}