package com.online.animall.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.animall.data.model.CreateUserResponse
import com.online.animall.data.model.SellAnimalResponse
import com.online.animall.data.repository.AnimalRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AnimalViewModel: ViewModel() {
    val repository = AnimalRepository()

    interface GetAnimalBreedCallback {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    interface SellAnimalCallback {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    interface GetAnimalCallback {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    private val _getAnimalResponse = MutableLiveData<Response<ResponseBody>?>()
    val getAnimalResponse: LiveData<Response<ResponseBody>?> get() = _getAnimalResponse

    fun getAnimals(token: String, callback: GetAnimalCallback) {
        viewModelScope.launch {
            try {
               val response =  repository.getAnimals(token)
                if(response.isSuccessful)
                    callback.onSuccess(response)
//                    _getAnimalResponse.value = response
                else
                    callback.onError(response.message())
//                    _getAnimalResponse.value = null
                Log.i("Response : Token", "${response.message()}, $token")
            } catch(exp: Exception) {
//                _getAnimalResponse.value = null
                callback.onError(exp.toString())
                Log.e("Error: ", exp.toString())
            }
        }
    }

    fun getAnimalBreed(token: String, search: String?, animalId: String, callback: GetAnimalBreedCallback) {
        viewModelScope.launch {
            try {
                val response =  repository.getAnimalBreed(token, search, animalId)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.message())
                Log.i("Response : Token", "${response.message()}, $token")
            } catch(exp: Exception) {
                callback.onError(exp.message.toString())
                Log.e("Error: ", exp.toString())
            }
        }
    }

    fun uploadSellAnimal(token: String, file: List<MultipartBody.Part>, request: SellAnimalResponse, callback: SellAnimalCallback) {
        viewModelScope.launch {
            try {
                val response = repository.uploadSellAnimal(token, request, file)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch(exp: Exception) {
                callback.onError(exp.message.toString())
                Log.i("Message: ", exp.message.toString())
            }
        }
    }


    /*fun getAnimals(token: String, callback: (ResponseBody?, String?) -> Unit) {
        viewModelScope.launch {
            try {
                repository.getAnimals(token).enqueue(object: Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if(response.isSuccessful)
                            callback.invoke(response.body(), null)
                        else
                            callback.invoke(null, "ErrorCode: ${response.code()}")
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        callback.invoke(null, "Error: ${t.message}")
                    }

                })
            } catch(exp: Exception) {
                callback.invoke(null, "Error: $exp")
            }
        }
    }*/
}