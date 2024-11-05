package com.online.animall.data.repository

import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.LocationRequest
import com.online.animall.data.model.OtpRequest
import com.online.animall.data.model.OtpResponse
import com.online.animall.data.model.UpdateProfile
import com.online.animall.data.remote.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Response

class UserRepository {

    /*suspend fun createUser(phoneNo: String): CreateUserResponse? {
        try {
            val request = CreateUserRequest(phoneNo)
            val response = RetrofitClient.api.createUser(request)
            return if (response.isSuccessful && response.code() == 200) response.body() else null
        } catch (e: Exception) {
            return null
        }
    }*/

    suspend fun createUser(phoneNo: String): CreateUserResponse {
        val request = CreateUserRequest(phoneNo)
        return RetrofitClient.api.createUser(request)
    }

    suspend fun verifyMobile(mobile: String, otp: String): OtpResponse {
        val request = OtpRequest(mobile, otp)
        return RetrofitClient.api.verifyMobile(request)
    }

    suspend fun sendLocation(longitude: String, latitude: String, token: String): CreateUserResponse {
        val request = LocationRequest(longitude, latitude)
        return RetrofitClient.api.saveLocation(token, request)
    }

    suspend fun regName(name: String, token: String): Boolean {
        val request = mapOf("name" to name)
        val response = RetrofitClient.api.regName(token, request)
        return response.isSuccessful
    }

    suspend fun getUserProfile(token: String): retrofit2.Response<ResponseBody> {
        return RetrofitClient.api.getUserProfile(token)
    }

    suspend fun getUserWorklist(token: String): retrofit2.Response<ResponseBody> {
        return RetrofitClient.api.getUserWorklist(token)
    }

    suspend fun getReasonOfUsingApp(token: String): retrofit2.Response<ResponseBody> {
        return RetrofitClient.api.getReasonsOfUsingApp(token)
    }

    suspend fun getEducationLevels(token: String): retrofit2.Response<ResponseBody> {
        return RetrofitClient.api.getEducationLevels(token)
    }

    suspend fun getAnimalHusbandry(token: String): retrofit2.Response<ResponseBody> {
        return RetrofitClient.api.getAnimalHusbandry(token)
    }

    suspend fun updateProfile(token: String, profileModel: UpdateProfile): Response<ResponseBody> {
        return RetrofitClient.api.updateProfile(
            token,
            profileModel.images,
            profileModel.name,
            profileModel.address,
            profileModel.whatsNumber,
            profileModel.birthday,
            profileModel.noOfAnimals,
            profileModel.workId,
            profileModel.animalHusbandryId,
            profileModel.reasonId,
            profileModel.educationLevelId
            )
    }

    /*suspend fun verifyMobile(mobile: String, otp: String): OtpResponse? {
        try {
            val request = OtpRequest(mobile, otp)
            val response = RetrofitClient.api.verifyMobile(request)
            Log.i("Response Code:", response.code().toString())
            return if (response.isSuccessful && response.code() == 200) response.body() else null
        } catch (e: Exception) {
            return null
        }
    }*/

    /*suspend fun sendLocation(
        longitude: String,
        latitude: String,
        token: String
    ): CreateUserResponse? {
        try {
            val request = LocationRequest(longitude, latitude)
            val response = RetrofitClient.api.saveLocation(token, request)
            return if (response.isSuccessful && response.code() == 200) response.body() else null
        } catch (e: Exception) {
            return null
        }
    }*/
}