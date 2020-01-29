package com.example.ContactsApp

import retrofit2.Call
import retrofit2.http.GET

interface PostAPIService {
    @GET("/users")
    fun getPostsFromName(): Call<List<Post>>
}