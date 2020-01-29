package com.example.ContactsApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_editar_contacto.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EditarContactoActivity : AppCompatActivity() {

    lateinit var name: String
    lateinit var db: AppDatabase
    private lateinit var contactByName: Contact
    private lateinit var contactByNameToUpdate: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_contacto)


        db = AppDatabase.getInstance(applicationContext)!!

        val intent = getIntent()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        name = intent.getStringExtra("name")
        loadContact(name)

        Thread.sleep(500)
        configView()

        bt_editar_guardar.setOnClickListener {

//            preparamos contacto para hacer el update
            contactByNameToUpdate.name = et_editar_name.text.toString()
            contactByNameToUpdate.email = et_editar_email.text.toString()
            contactByNameToUpdate.phone = et_editar_phone.text.toString()
            contactByNameToUpdate.website = et_editar_web.text.toString()
            contactByNameToUpdate.address?.street = et_editar_street.text.toString()
            contactByNameToUpdate.address?.zipcode = et_editar_zipcode.text.toString()
            contactByNameToUpdate.address?.city = et_editar_city.text.toString()

            updateContact()
            Thread.sleep(500)
//            intent de vuelta a la pantalla de contacto ( pasamos nombre para que se haga el update)
            val intentToDetail = Intent(this, DetalleContactoActivity::class.java)
            intentToDetail.putExtra("name", name)
            startActivity(intentToDetail)
        }


    }

    private fun updateContact() {
        GlobalScope.launch {
            //            pasamos valores de pantalla a variable para update.


            db.contactDao().update(contactByNameToUpdate)

        }
    }

    private fun configView() {
        et_editar_name.setText(contactByName.name)
        et_editar_email.setText(contactByName.email)
        et_editar_phone.setText(contactByName.phone)
        et_editar_web.setText(contactByName.website)
        et_editar_street.setText(contactByName.address?.street)
        et_editar_zipcode.setText(contactByName.address?.zipcode)
        et_editar_city.setText(contactByName.address?.city)
    }

    private fun loadContact(name: String) {

        GlobalScope.launch {
            //                recuperamos todos los contactos
            contactByName = db.contactDao().loadContactByName(name)
            contactByNameToUpdate = contactByName

        }
    }
}
