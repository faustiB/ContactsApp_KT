package com.example.ContactsApp

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_crear_contacto.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CrearContactoActivity : AppCompatActivity() {


    lateinit var db: AppDatabase
    //    Inicializamos variable para evitar dump
    private var adressTest: Address = Address("", "", "")
    private var contactToInsert: Contact = Contact("", "", "", "", adressTest)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_contacto)
        db = AppDatabase.getInstance(applicationContext)!!
//        creamos intent para volver a la pantalla de contactos, puesto que volveremos si o si ,
//        la diferencia es la inserción del contacto( dependiendo del listener triggeado )  .
        val intentToStart = Intent(this, ContactsListActivity::class.java)
        bt_crear_contacto.setOnClickListener {
            //            controlamos la obligatoriedad de los campos clave
            if (et_crear_name.text.isEmpty() || et_crear_email.text.isEmpty() || et_crear_phone.text.isEmpty() ||
                et_crear_name.text.isBlank() || et_crear_email.text.isBlank() || et_crear_phone.text.isBlank()
            ) {
                et_crear_name.setHintTextColor(Color.RED)
                et_crear_email.setHintTextColor(Color.RED)
                et_crear_phone.setHintTextColor(Color.RED)
                Toast.makeText(
                    this@CrearContactoActivity,
                    "Rellene los campos obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
//                comprobamos que los campos están llenos
            } else if (et_crear_name.text.isNotBlank() && et_crear_email.text.isNotBlank() && et_crear_phone.text.isNotBlank()) {
//                Insertamos contacto en la base de datos.
                insertContacto()
                Thread.sleep(500)
                startActivity(intentToStart)
                finish()
            }
        }
//        cancelamos acción y volvemos la pantalla de contactos.
        bt_cancelar_contacto.setOnClickListener {
            startActivity(intentToStart)
            finish()

        }
    }

    private fun insertContacto() {
        contactToInsert.name = et_crear_name.text.toString()
        contactToInsert.email = et_crear_email.text.toString()
        contactToInsert.phone = et_crear_phone.text.toString()
        contactToInsert.website = et_crear_web.text.toString()
        contactToInsert.address?.street = et_crear_street.text.toString()
        contactToInsert.address?.zipcode = et_crear_zipcode.text.toString()
        contactToInsert.address?.city = et_crear_city.text.toString()

        GlobalScope.launch {

            db.contactDao().insert(contactToInsert)
        }
    }

    override fun onBackPressed() {
        val backIntent = Intent(this, ContactsListActivity::class.java)
        startActivity(backIntent)
    }

}
