package com.online.animall.data.repository

import com.online.animall.data.remote.RetrofitClient
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class PostRepository {

    suspend fun createPost(token: String, image: List<MultipartBody.Part>, text: RequestBody, data_type: RequestBody): Response<ResponseBody> {
        return RetrofitClient.api.createPost(token, image, text, data_type)
    }

    suspend fun getAllPost(token: String): Response<ResponseBody> {
        return RetrofitClient.api.getAllPost(token)
    }

    suspend fun postLike(token: String, postId: String): Response<ResponseBody> {
        return RetrofitClient.api.postLike(token, postId)
    }

    suspend fun postComment(token: String, postId: String, comment: RequestBody): Response<ResponseBody> {
        return RetrofitClient.api.postComment(token, postId, comment)
    }

    suspend fun commentDelete(token: String, commentId: String, postId: String): Response<ResponseBody> {
        return RetrofitClient.api.deleteComment(token, commentId, postId)
    }

}