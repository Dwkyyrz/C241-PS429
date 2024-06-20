package com.TeamBangkit.animaldetection.Data.retrofit.API

import com.TeamBangkit.animaldetection.Data.retrofit.Response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIService {
    @Multipart
    @POST("/predict")
    fun predictAnimal(
        @Part Image: MultipartBody.Part
    ): Call<PredictionResponse>
}