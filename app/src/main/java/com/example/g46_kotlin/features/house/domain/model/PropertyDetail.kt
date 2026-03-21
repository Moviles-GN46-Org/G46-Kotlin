package com.example.g46_kotlin.features.house.domain.model

enum class PropertyType {
    APARTMENT,
    ROOM,
    STUDIO,
    HOUSE,
    SHARED_ROOM
}

enum class PropertyStatus {
    ACTIVE,
    RENTED,
    HIDDEN,
    EXPIRED
}

data class PropertyDetail(
    val id: String,
    val landlordId: String,
    val title: String,
    val description: String,
    val propertyType: PropertyType,
    val status: PropertyStatus = PropertyStatus.ACTIVE,

    val monthlyRent: Double,
    val depositAmount: Double?,
    val includesUtilities: Boolean = false,

    val address: String,
    val neighborhood: String,
    val city: String = "Bogota",
    val latitude: Double,
    val longitude: Double,

    val sizeM2: Double?,
    val bedrooms: Int,
    val bathrooms: Int,
    val furnished: Boolean = false,
    val petFriendly: Boolean = false,
    val hasParking: Boolean = false,
    val hasLaundry: Boolean = false,
    val hasWifi: Boolean = false,

    val imageUrls: List<String> = emptyList(),

    val publishedAt: String,
    val lastActiveAt: String,
    val expirationNotice: String?,
    val createdAt: String,
    val updatedAt: String
)