package com.example.ContactsApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_detalle_contacto.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DetalleContactoActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var db: AppDatabase
    private lateinit var contactByName: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_contacto)
        db = AppDatabase.getInstance(applicationContext)!!

        val intent = getIntent()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        name = intent.getStringExtra("name")
        loadContact(name)

        Thread.sleep(500)
        configView()
//        Click listener para phone, not working !
        tv_detalle_phone.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = (tv_detalle_phone.text.toString()).toUri()
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

        bt_detalle_ed_contacto.setOnClickListener {
            //            intent a pantalla de contacto con el nombre, recuperamos el contacto ahí y lo cargamos en el formulario de crear con los datos pertinentes.
            val intentToEdit = Intent(this, EditarContactoActivity::class.java)
            intentToEdit.putExtra("name", name)
            startActivity(intentToEdit)
        }
        bt_detalle_el_contacto.setOnClickListener {
//            intent a la pantalla de la lista de contactos y ejecutamos la eliminación del contacto
            Toast.makeText(this,"Contacto "+name.toString()+" eliminado",Toast.LENGTH_SHORT).show()
            val intentToStart = Intent(this, ContactsListActivity::class.java)
            deleteContact(contactByName)
            Thread.sleep(500)
            startActivity(intentToStart)
        }

    }

    private fun deleteContact(user: Contact) {
        GlobalScope.launch {
            //               eliminamos contacto
            db.contactDao().delete(user)

        }
    }

    private fun configView() {
        tv_detalle_name.text = contactByName.name
        tv_detalle_email.text = contactByName.email
        tv_detalle_phone.text = contactByName.phone
        tv_detalle_web.text = contactByName.website
        tv_detalle_street.text = contactByName.address?.street
        tv_detalle_zipcode.text = contactByName.address?.zipcode
        tv_detalle_city.text = contactByName.address?.city
    }

    private fun loadContact(name: String) {

        GlobalScope.launch {
            //                recuperamos todos los contactos
            contactByName = db.contactDao().loadContactByName(name)

        }
    }

    override fun onBackPressed() {
        val backIntent = Intent(this, ContactsListActivity::class.java)
        startActivity(backIntent)
    }

}
