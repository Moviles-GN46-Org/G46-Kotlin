package com.example.g46_kotlin.features.map.domain.model

data class Apartment(
    val id: String,
    val title: String,
    val description: String,
    val rating: Double,
    val lat: Double,
    val lon: Double,
    val price: String
)