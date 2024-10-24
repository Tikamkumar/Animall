package com.online.animall.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.animall.data.model.SellAnimalResponse
import com.online.animall.data.repository.AnimalRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response

class AnimalViewModel: ViewModel() {
    private val repository = AnimalRepository()

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

    interface ResponseCallback {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    private val _getAnimalResponse = MutableLiveData<Response<ResponseBody>?>()
    val getAnimalResponse: LiveData<Response<ResponseBody>?> get() = _getAnimalResponse

    fun getAnimalCategory(token: String, callback: GetAnimalCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getAnimalCategory(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody().toString())
//                    _getAnimalResponse.value = null
                Log.i("Response : Token", "${response.message()}, $token")
            } catch (exp: Exception) {
//                _getAnimalResponse.value = null
                callback.onError(exp.toString())
                Log.e("Error: ", exp.toString())
            }
        }
    }

    fun getLactation(token: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getLactation(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch (exp: Exception) {
                callback.onError(exp.toString())
            }
        }
    }

    fun getPregnantMonth(token: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getAnimalPregnant(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody().toString())
            } catch (exp: Exception) {
                callback.onError(exp.toString())
            }
        }
    }

    fun getAnimalBaby(token: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getAnimalBaby(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch (exp: Exception) {
                callback.onError(exp.toString())
            }
        }
    }

    fun getAnimalCalf(token: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getAnimalCalf(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody().toString())
            } catch (exp: Exception) {
                callback.onError(exp.toString())
            }
        }
    }

    fun getAnimalBreed(
        token: String,
        search: String?,
        animalId: String,
        callback: GetAnimalBreedCallback
    ) {
        viewModelScope.launch {
            try {
                val response = repository.getAnimalBreed(token, search, animalId)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody().toString())
                Log.i("Response : Token", "${response.message()}, $token")
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
                Log.e("Error: ", exp.toString())
            }
        }
    }

    fun uploadSellAnimal(
        token: String,
        file: List<MultipartBody.Part>,
        request: SellAnimalResponse,
        callback: SellAnimalCallback
    ) {
        viewModelScope.launch {
            try {
                val response = repository.uploadSellAnimal(token, request, file)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
                Log.i("Message: ", exp.message.toString())
            }
        }
    }

    fun getYourAnimal(token: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.getYourAnimals(token)
                if (response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody().toString())
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
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