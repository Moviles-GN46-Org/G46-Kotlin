package com.example.g46_kotlin.features.map.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PropertiesResponse(
    val success: Boolean,
    val data: PropertiesData
)
@Serializable
data class PropertiesData(
    val properties: List<PropertyDto>,
    val total: Int,
    val page: Int,
    val limit: Int,
    @SerialName("averageMonthlyRent")
    val avgRent: Double? = null
)

@Serializable
data class PropertyDto(
    val id: String,
    val title: String,
    val description: String,
    val propertyType: String,
    val status: String,
    val monthlyRent: Long,
    val depositAmount: Long? = null,
    val includesUtilities: Boolean,
    val address: String,
    val neighborhood: String,
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val sizeM2: Double,
    val bedrooms: Int,
    val bathrooms: Int,
    val furnished: Boolean,
    val petFriendly: Boolean,
    val hasParking: Boolean,
    val hasLaundry: Boolean,
    val hasWifi: Boolean,
    val imageUrls: List<String>,
    val landlord: LandlordDto,
    val averageRating: Double? = null,
    val reviewCount: Int,
    val publishedAt: String,
    val createdAt: String
)

@Serializable
data class LandlordDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String? = null,
    val isVerified: Boolean
)