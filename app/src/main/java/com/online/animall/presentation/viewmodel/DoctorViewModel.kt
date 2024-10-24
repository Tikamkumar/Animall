package com.online.animall.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.animall.data.repository.DoctorRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class DoctorViewModel: ViewModel() {

    val repository = DoctorRepository()

    interface ResponseCallBack {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    fun getNearDoctor(token: String, callback: ResponseCallBack) {
        viewModelScope.launch {
            try {
                val response = repository.getNearDoctor(token)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch(exp: Exception) {
                   callback.onError(exp.message!!)
            }
        }
    }

    fun getExpDoctor(token: String, callback: ResponseCallBack) {
        viewModelScope.launch {
            try {
                val response = repository.getExperienceDoctor(token)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch(exp: Exception) {
                callback.onError(exp.message!!)
            }
        }
    }
}