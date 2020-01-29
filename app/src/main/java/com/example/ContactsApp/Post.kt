package com.example.ContactsApp

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("phone") var phone: String,
    @SerializedName("website") var website: String,
    @SerializedName("address") var address: AddressC
)

data class AddressC(
    @SerializedName("street") var street: String,
    @SerializedName("city") var city: String,
    @SerializedName("zipcode") var zipcode: String
)

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey var name: String,
    @ColumnInfo(name = "email") var email: String?,
    @ColumnInfo(name = "phone") var phone: String?,
    @ColumnInfo(name = "website") var website: String?,
    @Embedded var address: Address?
) : Serializable

@Entity(tableName = "address")
data class Address(
    @ColumnInfo(name = "street") var street: String?,
    @ColumnInfo(name = "city") var city: String?,
    @ColumnInfo(name = "zipcode") var zipcode: String?
) : Serializable

