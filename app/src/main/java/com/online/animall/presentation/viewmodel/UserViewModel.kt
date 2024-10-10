package com.online.animall.presentation.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.OtpResponse
import com.online.animall.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(): ViewModel() {
    private val repository =  UserRepository()

    interface CreateUserCallback {
        fun onSuccess(response: CreateUserResponse)
        fun onError(error: String)
    }

    interface VerifyMobileCallback {
        fun onSuccess(response: OtpResponse)
        fun onError(error: String)
    }

    interface LocationCallback {
        fun onSuccess(response: CreateUserResponse)
        fun onError(error: String)
    }

    /*private val _createUserResponse = MutableLiveData<CreateUserResponse?>()
    val createUserResponse: LiveData<CreateUserResponse?> get() = _createUserResponse

    private val _otpResponse = MutableLiveData<OtpResponse?>()
    val otpResponse: LiveData<OtpResponse?> get() = _otpResponse

    private val _locationResponse = MutableLiveData<CreateUserResponse?>()
    val locationResponse: LiveData<CreateUserResponse?> get() = _locationResponse*/

    fun createUser(phoneNo: String, callback: CreateUserCallback) {
        viewModelScope.launch {
            try {
                val response = repository.createUser(phoneNo)
                callback.onSuccess(response)
            } catch (e: Exception) {
                callback.onError(e.message ?: "An error occurred")
            }
            /*val response = repository.createUser(phoneNo)
            _createUserResponse.value = response*/
        }
    }

    fun verifyMobile(mobile: String, otp: String, callback: VerifyMobileCallback) {
        viewModelScope.launch {
            try {
                val response = repository.verifyMobile(mobile, otp)
                callback.onSuccess(response)
            } catch (e: Exception) {
                callback.onError(e.message ?: "An error occurred")
            }
           /* val response = repository.verifyMobile(mobile, otp)
            _otpResponse.value = response*/
        }
    }

    fun sendLocation(longitude: String, latitude: String, token: String, callback: LocationCallback) {
        viewModelScope.launch {
            try {
                val response = repository.sendLocation(longitude, latitude, token)
                callback.onSuccess(response)
            } catch (e: Exception) {
                callback.onError(e.message ?: "An error occurred")
            }
         /*   val response = repository.sendLocation(longitude, latitude, token)
            _locationResponse.value = response*/
        }
    }

}