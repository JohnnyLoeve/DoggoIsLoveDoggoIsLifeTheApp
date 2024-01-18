package com.example.doggoislovedoggoislifetheapp.network
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiServiceRandom {
    @GET("breeds/image/random")
    suspend fun getDogPictures(@Query("count") count: Int): DogApiResponseRandom
}
