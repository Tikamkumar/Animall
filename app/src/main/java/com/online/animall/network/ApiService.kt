package com.online.animall.network

import com.online.animall.model.OtpRequest
import com.online.animall.model.OtpResponse
import com.online.animall.model.UserRequest
import com.online.animall.model.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("sign-up")
    suspend fun createUser(@Body userRequest: UserRequest): Response<UserResponse>

    @POST("varify-otp")
    suspend fun verifyMobile(@Body request: OtpRequest): Response<OtpResponse>
}

