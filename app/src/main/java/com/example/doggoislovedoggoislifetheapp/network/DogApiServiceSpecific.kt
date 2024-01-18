package com.example.doggoislovedoggoislifetheapp.network
import retrofit2.http.GET
import retrofit2.http.Path

interface DogApiServiceSpecific {
    @GET("breed/{breedName}/images")
suspend fun getDogPicturesSpecific(@Path("breedName") breedName: String): DogApiResponseSpecific
}

