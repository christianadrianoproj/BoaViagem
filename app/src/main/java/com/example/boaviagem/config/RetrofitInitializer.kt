package com.example.boaviagem.config

import com.example.boaviagem.api.IDataService
import com.google.gson.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitInitializer {

    val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()

    private val retrofit: Retrofit =  Retrofit.Builder()
        .baseUrl("https://api-mobile-android.herokuapp.com/")
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    fun Service() : IDataService {
        return retrofit.create(IDataService::class.java)
    }

}
