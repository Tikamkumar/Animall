package com.online.animall.data.model

import okhttp3.RequestBody

data class SellAnimalResponse(
    val animalId: RequestBody,
    val breedId: RequestBody,
    val lactation: RequestBody,
    val currentMilk: RequestBody,
    val milkCapacity: RequestBody,
    val price: RequestBody,
    val file_type_0 : RequestBody,
    val file_type_1 : RequestBody,
    val isNegotiable: RequestBody,
    val isPrime: RequestBody,
    val animalBaby: RequestBody,
    val info: RequestBody,
    val pregnent: RequestBody,
    val calfGender: RequestBody
)
