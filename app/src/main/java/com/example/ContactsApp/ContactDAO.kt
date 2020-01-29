package com.example.ContactsApp

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface ContactDAO {

    @Insert(onConflict = REPLACE)
    fun insert(contact: Contact):Long

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun loadAllContacts(): Array<Contact>

    @Query("SELECT * FROM contacts WHERE name = :name")
    fun loadContactByName(name: String): Contact

}