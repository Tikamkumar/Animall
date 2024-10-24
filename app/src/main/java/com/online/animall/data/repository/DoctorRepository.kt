package com.online.animall.data.repository

import com.online.animall.data.remote.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Response

class DoctorRepository {

    suspend fun getNearDoctor(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getNearDoctor(token)
    }

    suspend fun getExperienceDoctor(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getExperienceDoctor(token)
    }

}