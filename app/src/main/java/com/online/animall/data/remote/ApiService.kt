package com.online.animall.data.remote

import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.LocationRequest
import com.online.animall.data.model.OtpRequest
import com.online.animall.data.model.OtpResponse
import com.online.animall.data.model.SellAnimalResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

   /* @POST("sign-up")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>*/

    @POST("sign-up")
    suspend fun createUser(@Body request: CreateUserRequest): CreateUserResponse

    @POST("add-name")
    suspend fun regName(@Header("Authorization") token: String, @Body request: Map<String, String>): Response<Void>

    /* @POST("sign-up")
    suspend fun signUp(@Body request: CreateUserRequest): Response<SignUpResponse>
    data class SignUpResponse(
        val successMsg: String,
        val otp: String
    )*/

    @POST("varify-otp")
    suspend fun verifyMobile(@Body request: OtpRequest): OtpResponse

    @POST("user-location")
    suspend fun saveLocation(@Header("Authorization") token: String, @Body request: LocationRequest): CreateUserResponse

    /*@POST("get-animal")
    suspend fun getAnimals(@Header("Authorization") token: String): Call<ResponseBody>*/

    @GET("get-animal")
    suspend fun getAnimals(@Header("Authorization") token: String): Response<ResponseBody>

    @GET("animal-breed")
    suspend fun getAnimalBreed(
      @Header("Authorization") token: String,
      @Query("search") search: String?,
      @Query("animalId") animalId: String,
    ): Response<ResponseBody>

    @Multipart
    @POST("sell-animal")
    suspend fun uploadSellAnimal(
       @Header("Authorization") token: String,
       @Part file: List<MultipartBody.Part>,
       @Part("animalId") animalId: RequestBody,
       @Part("breedId") breedId: RequestBody,
       @Part("lactation") lactation: RequestBody,
       @Part("currentMilk") currentMilk: RequestBody,
       @Part("milkCapacity") milkCapacity: RequestBody,
       @Part("price") price: RequestBody,
       @Part("file_type_0") file_type_0: RequestBody,
       @Part("file_type_1") file_type_1: RequestBody,
       @Part("isNegotiable") isNegotiable: RequestBody,
       @Part("isPrime") isPrime: RequestBody,
       @Part("animalBaby") animalBaby: RequestBody,
       @Part("info") info: RequestBody,
       @Part("pregnent") pregnent: RequestBody,
       @Part("calfGender") calfGender: RequestBody,
    ): Response<ResponseBody>
}