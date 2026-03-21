package com.example.g46_kotlin.features.house.presentation

data class PropertyDetailUi(
    val id: String,
    val title: String,
    val description: String,
    val monthlyRent: Int,
    val depositAmount: Int?,
    val neighborhood: String,
    val address: String,
    val bedrooms: Int,
    val bathrooms: Int,
    val sizeM2: Double?,
    val furnished: Boolean,
    val petFriendly: Boolean,
    val hasParking: Boolean,
    val hasLaundry: Boolean,
    val hasWifi: Boolean,
    val includesUtilities: Boolean,
    val propertyType: String,
    val imageUrl: String? = null
)