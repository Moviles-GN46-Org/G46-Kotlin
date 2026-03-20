package com.example.g46_kotlin.features.house.domain.model

data class House(
    val id: String,
    val title: String,
    val pricePerMonth: Int,
    val rating: Double,
    val distanceToCampus: String,
    val propertyType: String,
    val isVerified: Boolean,
    val isLiked: Boolean,
    val imageUrl: String? = null
)
