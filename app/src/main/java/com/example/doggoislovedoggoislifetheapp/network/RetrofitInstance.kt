package com.example.doggoislovedoggoislifetheapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://dog.ceo/api/"

    val api: DogApiServiceRandom by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiServiceRandom::class.java)
    }
}

object RetrofitInstanceSpecific {
    private const val BASE_URL = "https://dog.ceo/api/"

    val api: DogApiServiceSpecific by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DogApiServiceSpecific::class.java)
    }
}
