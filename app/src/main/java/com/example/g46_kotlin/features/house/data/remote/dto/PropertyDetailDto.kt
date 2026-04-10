package com.example.g46_kotlin.features.house.data.remote.dto

import com.example.g46_kotlin.features.map.data.remote.dto.LandlordDto
import com.example.g46_kotlin.features.map.data.remote.dto.PropertyDto
import kotlinx.serialization.Serializable

@Serializable
data class PropertyDetailResponse(
    val success: Boolean,
    val data: PropertyDetailDto
)

@Serializable
data class PropertiesDetailResponse(
    val success: Boolean,
    val data: PropertiesDetailData
)
@Serializable
data class PropertiesDetailData(
    val properties: List<PropertyDetailDto>,
    val total: Int,
    val page: Int,
    val limit: Int
)

@Serializable
data class PropertyDetailDto(
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
data class LandlordHomeDto(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profilePictureUrl: String? = null,
    val isVerified: Boolean
)