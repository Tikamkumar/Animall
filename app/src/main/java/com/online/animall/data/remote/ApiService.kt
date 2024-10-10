package com.online.animall.data.remote

import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.LocationRequest
import com.online.animall.data.model.OtpRequest
import com.online.animall.data.model.OtpResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface ApiService {

   /* @POST("sign-up")
    suspend fun createUser(@Body request: CreateUserRequest): Response<CreateUserResponse>*/

    @POST("sign-up")
    suspend fun createUser(@Body request: CreateUserRequest): CreateUserResponse

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
}