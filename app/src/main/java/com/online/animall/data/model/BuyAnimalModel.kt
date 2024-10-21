package com.online.animall.data.model

data class BuyAnimalModel(
    val id: String,
    val userId: String,
    val animalId: String,
    val breedId: String,
    val lactation: String,
    val currentMilk: String,
    val milkCapacity: String,
    val price: String,
    val isNegotiable: String,
    val images: List<String>,
    val animalBaby: String,
    val isPregnent: String,
    val isCalfAvailable: String,
    val moreInfo: String,
    val createdAt: String
)
