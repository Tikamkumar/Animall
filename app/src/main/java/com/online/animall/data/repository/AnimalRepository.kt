package com.online.animall.data.repository

import com.online.animall.data.model.SellAnimalResponse
import com.online.animall.data.remote.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Multipart

class AnimalRepository {

    suspend fun getAnimalCategory(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getAnimalCategory(token)
    }

    suspend fun getLactation(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getLactation(token)
    }

    suspend fun getAnimalBaby(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getAnimalBaby(token)
    }

    suspend fun getAnimalCalf(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getAnimalCalf(token)
    }

    suspend fun getAnimalPregnant(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getAnimalPregnant(token)
    }

    suspend fun getAnimalBreed(
        token: String,
        search: String?,
        animalId: String
    ): Response<ResponseBody> {
        return RetrofitClient.api.getAnimalBreed(token, search, animalId)
    }

    suspend fun uploadSellAnimal(
        token: String,
        request: SellAnimalResponse,
        file: List<MultipartBody.Part>
    ): Response<ResponseBody> {
        return RetrofitClient.api.uploadSellAnimal(
            token,
            file,
            request.animalId,
            request.breedId,
            request.lactation,
            request.currentMilk,
            request.milkCapacity,
            request.price,
            request.file_type_0,
            request.file_type_1,
            request.isNegotiable,
            request.isPrime,
            request.animalBaby,
            request.info,
            request.pregnent,
            request.calfGender
        )
    }

    suspend fun getYourAnimals(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getYourAnimal(token)
    }
}