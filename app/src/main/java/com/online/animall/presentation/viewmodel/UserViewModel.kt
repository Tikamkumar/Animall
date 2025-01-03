package com.online.animall.presentation.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.common.api.Response
import com.online.animall.data.model.CreateUserRequest
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.OtpResponse
import com.online.animall.data.model.UpdateProfile
import com.online.animall.data.repository.UserRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

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

    interface ResponseCallback {
        fun onSuccess(response: retrofit2.Response<ResponseBody>)
        fun onError(error: String)
    }

    interface BooleanCallback {
        fun onSuccess(response: Boolean)
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

    fun getUserProfile(token: String, callBack: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.getUserProfile(token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

    fun getUserWorklist(token: String, callBack: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.getUserWorklist(token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

    fun getReasonOfUsingApp(token: String, callBack: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.getReasonOfUsingApp(token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

    fun getEducationLevels(token: String, callBack: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.getEducationLevels(token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

    fun getAnimalHusbandry(token: String, callBack: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.getAnimalHusbandry(token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

    fun updateProfile(token: String, model: UpdateProfile, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val res = repository.updateProfile(token, model)
                Log.i("Response : ", res.code().toString())
                if(res.isSuccessful)
                    callback.onSuccess(res)
                else
                    callback.onError(res.errorBody()!!.string())
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
                Log.i("Error : ", exp.toString())
            }
        }
    }

    fun regName(name: String, token: String, callBack: BooleanCallback) {
        viewModelScope.launch {
            try {
                val res = repository.regName(name, token)
                callBack.onSuccess(res)
            } catch(e: Exception) {
                callBack.onError(e.message.toString())
            }
        }
    }

}