package com.example.ContactsApp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ActivityLogin : AppCompatActivity() {

    lateinit var user_login: String
    lateinit var password_login: String
    lateinit var contacts_list: List<Post>
    lateinit var contact: Contact
    lateinit var address: Address
    lateinit var db: AppDatabase
    var finishedContacts: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

//        Listener del botoón de login
        bt_login.setOnClickListener {
            user_login = et_user.text.toString()
            password_login = et_password.text.toString()
//            comprobamos User/Password
            checkUserPassword(user_login, password_login)
            Thread.sleep(500)
//            Si se ha finalizado el import de contactos, pasamos al siguiente paso.
            if (finishedContacts) {
//                Intent a la lista de contactos
                val intent = Intent(this@ActivityLogin, ContactsListActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun checkUserPassword(user: String, password: String) {
//        Solo continuamos si el User/Password , es el que especificamos
        if (user != "admin" || password != "admin") {
            Toast.makeText(this, "Introduzca los datos de inicio correctos.", Toast.LENGTH_LONG).show()
        } else if (user == "admin" && password == "admin") {
//            Instanciamos base de datos
            db = AppDatabase.getInstance(applicationContext)!!
//            Coroutine para las querys / insert de datos
            GlobalScope.launch {
                //                recuperamos todos los contactos
                var contacts = db.contactDao().loadAllContacts()
//                miramos si hay contactos
                if (contacts.isEmpty()) {
//                    descargamos contactos de la API Rest
                    contacts_list = callUsersRetrofit()
//                    Recorremos la lista de contactos para ir insertandolos uno a uno en la DB
                    contacts_list.forEach {
                        //                        Creamos el contacto con los valores de la iteracion
                        address = Address(it.address.street, it.address.city, it.address.zipcode)
                        contact = Contact(it.name, it.email, it.phone, it.website, address)
                        var rowInserted = db.contactDao().insert(contact)
                        if (Integer.parseInt(rowInserted.toString()) != -1) {
                            println("New row added, row id: " + it.name)
                            finishedContacts = true
                        } else {
                            println("Something wrong")
                        }
                    }

                } else {
                    finishedContacts = true
                    contacts.forEach {
                        //                        Checkeamos en el log que la query ha funcionado correctamente.
                        println(
                            "NAME: " + it.name
                                    + "\tPHONE: " + it.phone
                                    + "\tEMAIL: " + it.email
                                    + "\tWEBSITE: " + it.website
                                    + "\tCITY: " + it.address!!.city
                                    + "\tSTREET: " + it.address!!.street
                                    + "\tZIPCODE: " + it.address!!.zipcode + "\n"
                        )

                    }
                }
            }
            Toast.makeText(this, "Contactos importados correctamente!", Toast.LENGTH_SHORT).show()
        }
    }

    fun callUsersRetrofit(): List<Post> {
//        GlobalScope.launch {
        //            val userId = "*"
        val call = getRetrofit().create(PostAPIService::class.java)
            .getPostsFromName().execute()

        val posts = call.body() as List<Post>


//        }
        return posts
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun onBackPressed() {

        val backIntent = Intent(Intent.ACTION_MAIN)
        backIntent.addCategory(Intent.CATEGORY_HOME)
        backIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(backIntent)

    }
}
