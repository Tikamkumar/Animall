package com.online.animall.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.online.animall.data.repository.PostRepository
import com.online.animall.presentation.viewmodel.AnimalViewModel.ResponseCallback
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response

class PostViewModel: ViewModel() {
    private val postResponse = MutableLiveData<Response<ResponseBody>?>()
    val _postResponse: LiveData<Response<ResponseBody>?> get() = postResponse

    private val repository = PostRepository()

    interface ResponseCallback {
        fun onSuccess(response: Response<ResponseBody>)
        fun onError(error: String)
    }

    fun createPost(
        token: String,
        image: List<MultipartBody.Part>,
        text: RequestBody,
        data_type: RequestBody,
        callback: com.online.animall.presentation.viewmodel.AnimalViewModel.ResponseCallback
    ) {
        viewModelScope.launch {
            try {
                val response = repository.createPost(token, image, text, data_type)

                if (response.isSuccessful) {
                    callback.onSuccess(response)
                } else {
                    val errorMsg =
                        JSONObject(response.errorBody()!!.string())["errorMsg"].toString()
                    callback.onError(errorMsg)
                }
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
            }
        }
    }

    fun getAllPost(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getAllPost(token)
                postResponse.value = response
            } catch (exp: Exception) {
                Log.e("Error: ", "Something went wrong..")
            }
        }
    }

    fun postLike(token: String, postId: String, callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.postLike(token, postId)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch (exp: Exception) {
                callback.onError(exp.message.toString())
            }
        }
    }

  fun postComment(token: String, postId: String, comment: RequestBody, callback: ResponseCallback) {
      viewModelScope.launch {
          try {
              val response = repository.postComment(token, postId, comment)
              if(response.isSuccessful)
                  callback.onSuccess(response)
              else
                  callback.onError(response.errorBody()!!.string())
          } catch (exp : Exception) {
              callback.onError(exp.message.toString())
          }
      }
  }

    fun deleteComment(token: String, commentId: String, postId: String,  callback: ResponseCallback) {
        viewModelScope.launch {
            try {
                val response = repository.commentDelete(token, commentId, postId)
                if(response.isSuccessful)
                    callback.onSuccess(response)
                else
                    callback.onError(response.errorBody()!!.string())
            } catch (exp : Exception) {
                callback.onError(exp.message.toString())
            }
        }
    }
}