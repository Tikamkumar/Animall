package com.online.animall.data.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class UpdateProfile(
    val name: RequestBody,
    val address: RequestBody,
    val whatsNumber: RequestBody,
    val birthday: RequestBody,
    val noOfAnimals: RequestBody,
    val workId: RequestBody,
    val animalHusbandryId: RequestBody,
    val reasonId: RequestBody,
    val educationLevelId: RequestBody,
    val images: MultipartBody.Part
)
