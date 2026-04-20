package com.example.g46_kotlin.features.map.domain.model

data class Property(
    val id: String,
    val title: String,
    val description: String,
    val rating: Double,
    val lat: Double,
    val lon: Double,
    val price: String,
    val image: String,
)

data class NearbyPropertiesResult(
    val properties: List<Property>,
    val avgRent: Double?
)